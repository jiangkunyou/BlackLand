package com.dlut.jky.app1.dao;

import com.dlut.jky.app1.beans.Algorithm;
import com.dlut.jky.app1.beans.Page;
import com.dlut.jky.app1.dao.impls.AlgorithmDbutilsDaoimpl;
import com.dlut.jky.app1.dao.impls.UserDbUtilsDaoImpl;
import com.dlut.jky.app1.dao.inters.AlgorithmDao;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.ResultSetHandler;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jiangkunyou on 15/10/29.
 */
public class DaoTest {

    @Test
    public void testString(){
        AlgorithmDao algorithmDao = new AlgorithmDbutilsDaoimpl();
        Page<Algorithm> page = algorithmDao.getPageByPageNoAndClassiferId(1, 3, 1);
        for(Algorithm algorithm: page.getItems()){
            System.out.println(algorithm.getAlgorName());
        }
    }

    @Test
    public void testUserQuery() throws Exception {
        UserDbUtilsDaoImpl userJdbcDao = new UserDbUtilsDaoImpl();
        Connection connection = JDBCTools.getConnection();
        String sql = "select * from user";
//        User user = userJdbcDao.get(connection, sql);
//        System.out.println(user);

        // 因为qr是线程安全的 所以可以在这里实例化一个对象，供下面一起使用
//    QueryRunner queryRunner = new QueryRunner();
        class myResultSetHandler implements ResultSetHandler {
            @Override
            public Object handle(ResultSet resultSet) throws SQLException {
                System.out.println("handler");
//            new BeanHandler<>()
//            new ScalarHandler()
                return null;
            }
        }
    }

//    public void testQueryRunnerQuery(){
//        Connection conn = null;
//        try {
//            String sql = "select * from user where userId = ?";
////            ResultSet rs = new ResultSetImpl();
//            conn = JDBCTools.getConnection();
////            Object obj = queryRunner.query(conn, sql, new myResultSetHandler(), 1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//
//        }
//    }

    @Test
    public void testListQuery(){
//        List<Algorithm> algorithms = DbUtilsDaoImpl.getForList(Algorithm.class, "select * from algorithm");
//        Algorithm algorithm = DbUtilsDaoImpl.get(Algorithm.class, "select * from algorithm where algorId = ?", 1);
//        for(Algorithm algorithm: algorithms){
//            System.out.println(algorithms);
//        }
    }

    @Test
    public void testC3P0() throws SQLException {
        DataSource cpds = new ComboPooledDataSource("KingCloudC3p0");
        System.out.println(cpds.getConnection());
    }


}
