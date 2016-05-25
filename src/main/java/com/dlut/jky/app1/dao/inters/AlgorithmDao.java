package com.dlut.jky.app1.dao.inters;

import com.dlut.jky.app1.beans.Algorithm;
import com.dlut.jky.app1.beans.Page;

import java.util.List;

/**
 * Created by jiangkunyou on 15/11/8.
 */
public interface AlgorithmDao {

    // 插入算法
    void insertAlgorithm(Algorithm algorithm);

    // 删除算法
    void deleteAlgorithm(Integer id);

    // 更新算法
    void updateAlgorithm(Algorithm algorithm);

    // 通过算法id获取算法
    Algorithm getAlgorithmById(Integer id);

    // 获取所有算法集合
    List<Algorithm> getAlgorithms(Object ... args);

    // 获取某个类别的算法数, classiferId为负数时代表所有类别
    long getTotalAlgorithmNumberByClassiferId(int classiferId);

    // 获取跟搜索框关键字匹配的算法数量
    long getTotalAlgorithmNumberByKeyword(String keyword);

    /**
     * 通过pageNo和算法类别编号获取某一页的page对象,classiferId为负数时代表所有类别
     * @param pageNo: 页码
     * @param pageSize: 每一页元素个数
     * @param classiferId: 算法类别
     */
    Page<Algorithm> getPageByPageNoAndClassiferId(int pageNo, int pageSize, int classiferId);

    // 根据页码和关键字获取page对象
    Page<Algorithm> getPageByPageNoAndKeyword(int pageNo, int pageSize, String keyword);
}
