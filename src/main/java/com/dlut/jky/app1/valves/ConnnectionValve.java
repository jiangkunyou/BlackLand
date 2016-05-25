package com.dlut.jky.app1.valves;

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.service.pipeline.Valve;
import com.dlut.jky.app1.dao.ConnectionManager;
import com.dlut.jky.app1.dao.JDBCTools;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 15/11/26.
 * 过滤所有连接,在请求发给servlet前,把每个线程跟他的connection进行绑定
 */
public class ConnnectionValve implements Valve{
    @Override
    public void invoke(PipelineContext pipelineContext) throws Exception {
        Connection connection = JDBCTools.getConnection();
//        System.out.println("----------------myValves starts!-----------");
//        System.out.println("--------------thread: " + Thread.currentThread().getName());
        try {
            connection.setAutoCommit(false);
            // 绑定线程自用的connection,这样该线程里的所有dao操作都可以共用一个connection了
            ConnectionManager.getInstance().bind(connection);
            // 继续执行后面的valve
            pipelineContext.invokeNext();
            connection.commit();
//            System.out.println("-------------myValves ends!-----------");
        } catch (Exception  e) {
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
//            System.out.println("--------------myValves finally");
        }
    }
}
