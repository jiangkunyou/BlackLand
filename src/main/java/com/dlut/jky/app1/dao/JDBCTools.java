package com.dlut.jky.app1.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * JDBC �Ĺ�����
 * 
 * ���а���: ��ȡ���ݿ�����, �ر����ݿ���Դ�ȷ���.
 */
public class  JDBCTools {

    /**
     * ���ӳض��󣬱���Ŀ��ֻ��Ҫһ�����������Ӿ��������ɺ͹���
     */
    private static DataSource dataSource = null;
    static {
        dataSource = new ComboPooledDataSource("BlackLandC3P0");
    }

    /**
     * �������ӳػ�ȡ����
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
     * �ͷ����ӳ��е�һ������
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
//		// 1. ׼����ȡ���ӵ� 4 ���ַ���: user, password, jdbcUrl, driverClass
//		String user = properties.getProperty("user");
//		String password = properties.getProperty("password");
//		String jdbcUrl = properties.getProperty("jdbcUrl");
//		String driverClass = properties.getProperty("driverClass");
//
//		// 2. ��������: Class.forName(driverClass)
//		Class.forName(driverClass);
//
//		// 3. ����
//		// DriverManager.getConnection(jdbcUrl, user, password)
//		// ��ȡ���ݿ�����
//		Connection connection = DriverManager.getConnection(jdbcUrl, user,
//				password);
//		return connection;
//	}

}
