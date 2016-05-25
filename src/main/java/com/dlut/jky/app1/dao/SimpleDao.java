package com.dlut.jky.app1.dao;

import jodd.bean.BeanUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangkunyou on 15/10/31.
 * 没有使用特殊工具实现的dao类
 */
public class SimpleDao {

    public static void update(String sql, Object ... args) {
        Connection conn = null;
        PreparedStatement pstat = null;
        try {
            conn = JDBCTools.getConnection();
            pstat = conn.prepareStatement(sql);
            for(int i = 0; i < args.length; i++){
                pstat.setObject(i + 1, args[i]);
            }
            pstat.executeUpdate();
        } catch (Exception e) {
            System.out.println("DbUtilsDaoImpl update method fail");
            e.printStackTrace();
        } finally {
            JDBCTools.releaseDB(null, pstat, conn);
        }
    }

    /**
     * 根据sql获取任意bean对象实例
     */
    public static <T> T get(Class<T> clazz, String sql, Object ... args) {
        T entity = null;
        Connection conn = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        try {
            conn = JDBCTools.getConnection();
            pstat = conn.prepareStatement(sql);
            for(int i = 0; i < args.length; i++){
                pstat.setObject(i + 1, args[i]);
            }
            rs = pstat.executeQuery();
            if(rs.next()){
                List<String> labels = getColumnLabels(rs);
                entity = getAEntity(clazz, labels, rs);
            }
        } catch (Exception e) {
            System.out.println("DbUtilsDaoImpl get method fail");
            e.printStackTrace();
        } finally {
            JDBCTools.releaseDB(rs, pstat, conn);
        }
        return entity;
    }

    /**
     * 获取类型为T的对象实例数组
     */
    public static <T> List<T> getForList(Class<T> clazz, String sql, Object ... args){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<T> entities = new ArrayList<T>();
        try {
            connection = JDBCTools.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for(int i = 0; i < args.length; i++){
                preparedStatement.setObject(i + 1, args[i]);
            }
            ResultSet rs = preparedStatement.executeQuery();
            List<String> labels = getColumnLabels(rs);
            while(rs.next()){
                entities.add(getAEntity(clazz, labels, rs));
            }
        } catch (Exception e) {
            System.out.println("DbUtilsDaoImpl List method fail!");
            e.printStackTrace();
        }
        return entities;
    }

    /**
     * 获取rs记录的某一个值
     */
    public static <E> E getForValue(String sql, Object ... args){
        Connection conn = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        try {
            conn = JDBCTools.getConnection();
            pstat = conn.prepareStatement(sql);
            for(int i = 0; i < args.length; i++){
                pstat.setObject(i + 1, args[i]);
            }
            rs = pstat.executeQuery();
            if(rs.next()){
                return (E)rs.getObject(1);
            }
        } catch (Exception e) {
            System.out.println("DbUtilsDaoImpl getForValue method fail");
            e.printStackTrace();
        } finally {
            JDBCTools.releaseDB(rs, pstat, conn);
        }
        return null;
    }

    /**
     * 获取类属性别名，这里一般把别名设置成跟bean对象属性一样的名字
     * 这里的sql语句一般也要在每个属性后面就如别名，如select id userId...
     */
    private static List<String> getColumnLabels(ResultSet rs){
        List<String> list = new ArrayList<String>();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            for(int i = 0; i < rsmd.getColumnCount(); i++){
                list.add(rsmd.getColumnLabel(i + 1));
            }
        } catch (SQLException e) {
            System.out.println("getColumLabels method fail!");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据元组属性别名和rs，创建一个实体bean对象并返回
     */
    private static <T> T getAEntity(Class<T> clazz, List<String> labels, ResultSet rs) throws Exception{
        T entity = clazz.newInstance();
        for(String label: labels){
            BeanUtil.setDeclaredProperty(entity, label, rs.getObject(label));
        }
        return entity;
    }
}
