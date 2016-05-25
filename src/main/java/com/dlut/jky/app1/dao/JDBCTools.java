package com.dlut.jky.app1.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * JDBC 的工具类
 * 
 * 其中包含: 获取数据库连接, 关闭数据库资源等方法.
 */
public class  JDBCTools {

    /**
     * 连接池对象，本项目中只需要一个，所有连接均有它生成和管理
     */
    private static DataSource dataSource = null;
    static {
        dataSource = new ComboPooledDataSource("BlackLandC3P0");
    }

    /**
     * 利用连接池获取链接
     */
    public static Connection getConnection(){

        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            Logger.getGlobal().info("");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 释放连接池中的一条连接
     */
    public static void releaseConn(Connection conn){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void releaseDB(ResultSet resultSet, Statement statement,
                                 Connection connection) {

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

//	public static Connection getConnection() throws Exception {
//		Properties properties = new Properties();
//		InputStream inStream = JDBCTools.class.getClassLoader()
//				.getResourceAsStream("jdbc.properties");
//		properties.load(inStream);
//
//		// 1. 准备获取连接的 4 个字符串: user, password, jdbcUrl, driverClass
//		String user = properties.getProperty("user");
//		String password = properties.getProperty("password");
//		String jdbcUrl = properties.getProperty("jdbcUrl");
//		String driverClass = properties.getProperty("driverClass");
//
//		// 2. 加载驱动: Class.forName(driverClass)
//		Class.forName(driverClass);
//
//		// 3. 调用
//		// DriverManager.getConnection(jdbcUrl, user, password)
//		// 获取数据库连接
//		Connection connection = DriverManager.getConnection(jdbcUrl, user,
//				password);
//		return connection;
//	}

}
