package com.dlut.jky.app1.module.action;

import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.dlut.jky.app1.utils.FileUploadManager;
import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jiangkunyou on 15/11/20.
 */
public class FileUploadAction {

    public void doUpload(TurbineRunData runData) {
        // 获取form中的文件域内容
        List<FileItem> items = Arrays.asList(runData.getParameters().getFileItems("inputfile"));
        // 获取存放上传文件的根路径
        String fileSavePath = runData.getRequest().getServletContext().getRealPath(FileUploadManager.getInstance().getProperty("fileSavePath"));
        /**
         * 在webx.xml中配置upload对象的参数
         * <services:upload sizeMax="5G"  HTTP请求的最大尺寸（字节，支持K/M/G），超过此尺寸的请求将被抛弃。值-1表示没有限制。
         *                  fileSizeMax="1G"  单个文件允许的最大尺寸（字节，支持K/M/G），超过此尺寸的文件将被抛弃。值-1表示没有限制。
         *                  repository="/WEB-INF/temp"  暂存上传文件的目录。
         *                  sizeThreshold="10M" />  将文件放在内存中的阈值（字节，支持K/M/G），小于此值的文件被保存在内存中.
         */
        FileUploadManager.getInstance().doFileUpload(items, fileSavePath, runData.getRequest().getParameter("algorId"));
//        runData.getRequest().getParameter()
    }
}
