package com.dlut.jky.app1.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jiangkunyou on 15/11/19.
 */
public class FileUploadServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("--------------- do post");
//        FileUploadManager.getInstance().doFileUpload(req);
        req.getRequestDispatcher("fileupload.vm").forward(req, resp);
    }

}
