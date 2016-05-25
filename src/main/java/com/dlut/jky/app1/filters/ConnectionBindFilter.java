package com.dlut.jky.app1.filters;

import com.alibaba.citrus.webx.servlet.FilterBean;
import com.dlut.jky.app1.dao.ConnectionManager;
import com.dlut.jky.app1.dao.JDBCTools;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 15/11/14.
 * 过滤所有连接,在请求发给servlet前,把每个线程跟他的connection进行绑定
 */
public class ConnectionBindFilter extends FilterBean{

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Connection connection = JDBCTools.getConnection();
//        System.out.println("--------------thread: " + Thread.currentThread().getName() + " url: " + request.getServletPath() + " doFilter");
        try {
            connection.setAutoCommit(false);
            // 绑定线程自用的connection,这样该线程里的所有dao操作都可以共用一个connection了
            ConnectionManager.getInstance().bind(connection);
            chain.doFilter(request, response);
            connection.commit();
        } catch (SQLException e) {
            Logger.getGlobal().info("");
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            // 解除绑定
            ConnectionManager.getInstance().remove();
            JDBCTools.releaseConn(connection);
        }
    }
}
