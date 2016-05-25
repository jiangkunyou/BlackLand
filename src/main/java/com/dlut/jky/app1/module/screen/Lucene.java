package com.dlut.jky.app1.module.screen;

import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.dlut.jky.app1.utils.ExcelAndLuceneUtils;
import com.dlut.jky.app1.utils.FileUploadManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 16/4/25.
 */
public class Lucene {

    @Autowired
    private HttpServletResponse response;

    public void execute(TurbineRunData runData, @Param("classId") String classId) throws IOException {
        if("1".equals(classId)){
            doFillAlgorithm(runData);
        }
    }

    public void doFillAlgorithm(TurbineRunData runData){
        try {
            String filePath = FileUploadManager.getInstance().getFinalFilePath();
            String filePathWithoutSuffix = filePath.substring(0, filePath.lastIndexOf('/') + 1);
            Runtime.getRuntime().exec("unzip -o " + filePath + " -d" + filePathWithoutSuffix);
//            System.out.println("-----------------" + filePath + "---------------");
            String originFileName = filePath.substring(filePath.indexOf('_') + 1, filePath.lastIndexOf('.'));
//            System.out.println("------------=====----" + filePathWithoutSuffix + originFileName + "------------");
            String outputPath = filePathWithoutSuffix + originFileName;
            FileUploadManager.getInstance().setBlackLandOutputPath(outputPath);
            ExcelAndLuceneUtils.myGod(outputPath);
            Runtime.getRuntime().exec("zip -r " + outputPath + ".zip " + outputPath);
            response.getWriter().print("success");
        } catch (Exception e) {
            try {
                response.getWriter().print("failed");
            } catch (IOException e1) {
                Logger.getGlobal().info("");
                e1.printStackTrace();
            }
            Logger.getGlobal().info("");
            e.printStackTrace();
        } finally {
//            ExcelAndLuceneUtils.fixedPool.shutdown();
//            ExcelAndLuceneUtils.fixedPool = null;
        }
    }
}
