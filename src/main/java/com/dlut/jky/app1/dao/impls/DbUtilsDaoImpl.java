package com.dlut.jky.app1.dao.impls;

import com.dlut.jky.app1.dao.ConnectionManager;
import com.dlut.jky.app1.dao.JDBCTools;
import com.dlut.jky.app1.dao.inters.DAO;
import com.dlut.jky.app1.utils.ReflectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 15/10/29.
 * 使用DBUtils工具类实现的DAO
 */
public class DbUtilsDaoImpl<T> implements DAO<T> {

    private QueryRunner queryRunner = null;
    private Class<T> type;

    public DbUtilsDaoImpl(){
        queryRunner = new QueryRunner();
        type = ReflectionUtils.getSuperGenericType(getClass());
    }

    @Override
    public void update(String sql, Object... args){
        Connection connection = null;
        try {
            connection = ConnectionManager.getInstance().getConnection();
            queryRunner.update(connection, sql, args);
        } catch (Exception e) {
            Logger.getGlobal().info("update error!");
            e.printStackTrace();
        }
    }

    @Override
    public int insert(String sql, Object ... args) {
        Connection connection = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        int id = 0;
        try {
            connection = ConnectionManager.getInstance().getConnection();
            pstat = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            for(int i = 0; i < args.length; i++){
                pstat.setObject(i + 1, args[i]);
            }
            pstat.executeUpdate();
            rs = pstat.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }
        } catch (Exception e) {
            Logger.getGlobal().info("");
            e.printStackTrace();
        } finally {
            JDBCTools.releaseDB(rs, pstat, null);
        }
        return id;
    }

    @Override
    public T get(String sql, Object... args){

        System.out.println("-------------" + Thread.currentThread().getName() + " get");
        Connection connection = null;
        try {
            connection = ConnectionManager.getInstance().getConnection();
            return (T) queryRunner.query(connection, sql, new BeanHandler(type), args);
        } catch (Exception e) {
            Logger.getGlobal().info("get error!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<T> getForList(String sql, Object... args){
        Connection connection = null;
        try {
            connection = ConnectionManager.getInstance().getConnection();
            return (List<T>) queryRunner.query(connection, sql, new BeanListHandler(type), args);
        } catch (Exception e) {
            Logger.getGlobal().info("getForList error!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <E> E getForValue(String sql, Object... args){
        Connection connection = null;
        try {
            connection = ConnectionManager.getInstance().getConnection();
            return (E) queryRunner.query(connection, sql, new ScalarHandler(), args);
        } catch (Exception e) {
            Logger.getGlobal().info("getForValue error!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void batch(String sql, Object[]... args){
        Connection connection = null;
        try {
            connection = ConnectionManager.getInstance().getConnection();
            queryRunner.batch(connection, sql, args);
        } catch (Exception e) {
            Logger.getGlobal().info("batch error!");
            e.printStackTrace();
        }
    }

}
