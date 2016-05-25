package com.dlut.jky.app1.dao.inters;

import java.util.List;

/**
 * Created by jiangkunyou on 15/10/30
 * 访问数据的DAO接口
 * 定义各种访问数据表的方法
 * @param T: DAO 处理的实体类的类型
 */
public interface DAO<T> {

    /**
     * INSERT, UPDATE, DELETE
     * @param sql: SQL语句
     * @param args: 查询参数
     */
    void update(String sql, Object ... args);

    /**
     * 该insert方法单独提出来,是为了在插入操作后返回自动生成的id值
     */
    int insert(String sql, Object ... args);

    /**
     * 返回一个T对象
     */
    T get(String sql, Object ... args);

    /**
     * 返回T对象集合
     */
    List<T> getForList(String sql, Object ... args);

    /**
     * 返回具体查询值
     */
    <E> E getForValue(String sql, Object ... args);

    /**
     *
     */
    void batch(String sql, Object [] ... args);


}
