package com.dlut.jky.app1.dao;

import lombok.Setter;

import java.sql.Connection;

/**
 * Created by jiangkunyou on 15/11/25.
 * 管理连接,在需要进行事务处理时,需要同时执行多个dao操作,
 * 需要让他们共用一个connection,所以需要把每个线程跟他的connection进行绑定
 * 该类为单例
 */
public class ConnectionManager {

    private ConnectionManager(){}

    // 保存当前线程对应的那个connection
    @Setter private ThreadLocal<Connection> threadLocal;

    private static class ConnectionManagerHolder{
        static ConnectionManager singleton;
        static {
            singleton = new ConnectionManager();
            singleton.threadLocal = new ThreadLocal<Connection>();
        }
    }

    public static ConnectionManager getInstance(){
        return ConnectionManagerHolder.singleton;
    }

    // 绑定到当前线程
    public void bind(Connection conn){
        threadLocal.set(conn);
    }

    // 获取当前线程的connection
    public Connection getConnection(){
        return threadLocal.get();
    }

    // 删除绑定
    public void remove(){
        threadLocal.remove();
    }

}
