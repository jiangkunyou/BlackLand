package com.dlut.jky.app1.module.screen;

import com.alibaba.citrus.service.requestcontext.buffered.BufferedRequestContext;
import com.alibaba.citrus.service.requestcontext.rundata.RunData;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.dlut.jky.app1.utils.FileUploadManager;
import com.dlut.jky.app1.utils.SSHHelper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 15/11/30.
 */
public class FileDownload {

    @Autowired
    private BufferedRequestContext buffered;

    @Autowired
    private HttpServletResponse response;

    public void execute(TurbineRunData runData) throws IOException {
        doDownload(runData);
    }

    // 获取要下载文件的输入流, 然后读取并输出
    private void doDownload(TurbineRunData runData) throws IOException {
        buffered.setBuffering(false);
        FileInputStream fin = null;
        OutputStream os = null;
        try {
            String filePath = FileUploadManager.getInstance().getBlackLandOutputPath() + ".zip";
//            String filePath = "/Users/jiangkunyou/IdeaProjects/BlackLand/src/main/resources/blackLand.zip";
            os = response.getOutputStream();
            fin = new FileInputStream(filePath);
//            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filePath, "UTF-8"));
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment; filename=\"output.zip\"");
            IOUtils.copy(fin, os);
            os.flush();
//            response.getWriter().print("success");
        } catch (IOException e) {
//            response.getWriter().print("failed");
            Logger.getGlobal().info("");
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fin);
            IOUtils.closeQuietly(os);
        }
    }

}
