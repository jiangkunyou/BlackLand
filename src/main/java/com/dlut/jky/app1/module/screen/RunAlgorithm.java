package com.dlut.jky.app1.module.screen;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.dlut.jky.app1.Services.AlgorithmService;
import com.dlut.jky.app1.Services.ClassiferService;
import com.dlut.jky.app1.beans.Algorithm;
import com.dlut.jky.app1.beans.Classifer;
import com.dlut.jky.app1.utils.ReadFromFile;
import com.dlut.jky.app1.utils.SSHHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 15/12/19.
 */
public class RunAlgorithm {

    @Autowired
    private ClassiferService classiferService;

    public void execute(@Param("classId") String classId, Context context, Navigator nav) {
//暂时注释掉,完成可视化后,改回来
        injectClassifer(classId, context, nav);
        // 调试的时候用
        SSHHelper.ssh("source /etc/profile;hadoop fs -rmr /user/output/*");
        // 暂时注释掉
//        if(algorId.equals("14")){
            fillFirstFileName(context);
//        }
    }

    private void injectClassifer(String classId, Context context, Navigator nav){
        try {
            Classifer classifer = classiferService.getClassiferById(Integer.parseInt(classId));
            context.put("classifer", classifer);
        } catch (Exception e) {
            Logger.getGlobal().info("");
            e.printStackTrace();
            nav.redirectTo("app1Link").withTarget("404.vm");
        }
    }

    // LDA算法生成docIndex文件,取第一个文件名
    private void fillFirstFileName(Context context){
        String filePath = "/Users/jiangkunyou/IdeaProjects/KingCloud/target/KingCloud-1.0-SNAPSHOT/WEB-INF/upload/output/mydata-docIndex.txt";
        String value = ReadFromFile.readFileByLineNumber(filePath, 3);
        String fileName = value.split(": ")[3];
        fileName = fileName.substring(fileName.indexOf('/') + 1);
        context.put("fileName", fileName);
    }
}
