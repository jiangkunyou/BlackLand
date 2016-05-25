package com.dlut.jky.app1.module.screen;

import com.alibaba.citrus.turbine.dataresolver.Param;
import com.dlut.jky.app1.Services.AlgorithmService;
import com.dlut.jky.app1.beans.Algorithm;
import com.dlut.jky.app1.beans.MyLog;
import com.dlut.jky.app1.module.screen.simple.Count;
import com.dlut.jky.app1.utils.FileUploadManager;
import com.dlut.jky.app1.utils.SSHHelper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 15/12/16.
 */
public class Hadoop {

    @Autowired
    private AlgorithmService algorithmService;

    @Autowired
    private HttpServletResponse response;

    // 记录每次读取日志的偏移量
    private static long offset_int = 0;

    // 文件随机读写对象
    private static RandomAccessFile raf = null;

    public void doAlgorithmOnHadoop(@Param("algorId") String algorId){
        try {
            // 获取需要运行的算法的mahout命令
            Algorithm algorithm = algorithmService.getAlgorithmById(Integer.parseInt(algorId));
            SSHHelper.ssh("source /etc/profile;" + algorithm.getAlgorCommand());
            response.getWriter().print("success");
        } catch (IOException e) {
            try {
                response.getWriter().print("failed");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Logger.getGlobal().info("");
            e.printStackTrace();
        }
    }

    public void doReadLog(){
        byte [] buf = new byte[40960];
        try {
            if(raf == null){
                // 获取日志绝对路径
                String logPath = FileUploadManager.getInstance().getLogPath() + "/log";
                raf = new RandomAccessFile(logPath, "r");
            }
            raf.seek(offset_int);
            int len = raf.read(buf, 0, 40960);
            // 判断是否结束输出
            if(SSHHelper.flag && offset_int >= raf.length()){
                // 为了跟读到文件末尾的-1区分开,这里用-2
                transJson(buf, -2);
                offset_int = 0;
                raf.close();
                raf = null;
                return;
            }
            transJson(buf, len);
            if(len != -1){
                offset_int += len;
            }
        } catch (Exception e) {
            try {
                // 为了跟读到文件末尾的-1和结束的-2区分开,这里用-3
                transJson(buf, -3);
                raf.close();
                raf = null;
            } catch (IOException e1) {
                Logger.getGlobal().info("");
                e1.printStackTrace();
            }
            Logger.getGlobal().info("");
            e.printStackTrace();
        }
    }

    // 将myLog转换成json并传回js文件
    private void transJson(byte [] buf, int len) throws IOException {
        MyLog myLog = new MyLog(new String(buf), String.valueOf(len));
        Gson gson = new Gson();
        String jsonStr = gson.toJson(myLog);
        response.getWriter().print(jsonStr);
    }

}
