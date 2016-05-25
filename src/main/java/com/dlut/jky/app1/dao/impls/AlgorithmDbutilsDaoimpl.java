package com.dlut.jky.app1.dao.impls;

import com.dlut.jky.app1.beans.Algorithm;
import com.dlut.jky.app1.beans.Page;
import com.dlut.jky.app1.dao.inters.AlgorithmDao;

import java.util.List;

/**
 * Created by jiangkunyou on 15/11/8.
 */
public class AlgorithmDbutilsDaoimpl extends DbUtilsDaoImpl<Algorithm> implements AlgorithmDao {
    @Override
    public void insertAlgorithm(Algorithm algorithm) {
        String sql = "INSERT INTO algorithm(algorName, classId, algorAuthor, algorModify, algorDescrib, algorCareful, algorCommand, className) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        update(sql, algorithm.getAlgorName(), algorithm.getClassId(), algorithm.getAlgorAuthor(), algorithm.getAlgorModify(), algorithm.getAlgorDescrib(), algorithm.getAlgorCareful(), algorithm.getAlgorCommand(), algorithm.getClassName());
    }

    @Override
    public void deleteAlgorithm(Integer id) {
        String sql = "DELETE FROM algorithm WHERE algorId = ?";
        update(sql, id);
    }

    @Override
    public void updateAlgorithm(Algorithm algorithm) {
        String sql = "UPDATE algorithm SET algorName = ?, classId = ?, algorAuthor = ?, algorModify = ?, algorDescrib = ?, algorCareful = ?, algorCommand = ?, className = ? WHERE algorId = ?";
        update(sql, algorithm.getAlgorName(), algorithm.getClassId(), algorithm.getAlgorAuthor(), algorithm.getAlgorModify(), algorithm.getAlgorDescrib(), algorithm.getAlgorCareful(), algorithm.getAlgorCommand(), algorithm.getClassName(), algorithm.getAlgorId());
    }

    @Override
    public Algorithm getAlgorithmById(Integer id) {
        String sql = "SELECT * FROM algorithm WHERE algorId = ?";
        return get(sql, id);
    }

    @Override
    public List<Algorithm> getAlgorithms(Object... args) {
        String sql = "SELECT * FROM algorithm";
        return getForList(sql, args);
    }

    @Override
    public long getTotalAlgorithmNumberByClassiferId(int classiferId) {
        String sql1 = "SELECT COUNT(*) FROM algorithm WHERE classId = ?";
        String sql2 = "SELECT COUNT(*) FROM algorithm";
        // classId为负数时,获取所有类别算法总数,这里注意要返回long,用int出错
        if(classiferId < 0){
            return getForValue(sql2);
        }
        return getForValue(sql1, classiferId);
    }

    @Override
    public long getTotalAlgorithmNumberByKeyword(String keyword) {
        String sql = "SELECT COUNT(*) FROM algorithm WHERE algorName LIKE %?%";
        return getForValue(sql, keyword);
    }

    @Override
    public Page<Algorithm> getPageByPageNoAndClassiferId(int pageNo, int pageSize, int classiferId) {

        String sql1 = "SELECT * FROM algorithm WHERE classId = ? LIMIT ?,?";
        String sql2 = "SELECT * FROM algorithm LIMIT ?,?";

        long totalAlgorithmNumber = getTotalAlgorithmNumberByClassiferId(classiferId);
        Page<Algorithm> page = new Page<Algorithm>(pageNo, pageSize, totalAlgorithmNumber);
        List<Algorithm> list = null;

        // 计算当前页前面有多少个元素,作为limit的第一个参数
        int prevNumber = (page.getPageNo() - 1) * pageSize;
        if(classiferId < 0){
            list = getForList(sql2, prevNumber, pageSize);
        } else {
            list = getForList(sql1, classiferId, prevNumber, pageSize);
        }
        page.setItems(list);
        return page;
    }

    @Override
    public Page<Algorithm> getPageByPageNoAndKeyword(int pageNo, int pageSize, String keyword) {
        String sql = "SELECT * FROM algorithm WHERE algorName Like %?% LIMIT ?,?";
        long totalAlgorithmNumber = getTotalAlgorithmNumberByKeyword(keyword);
        Page<Algorithm> page = new Page<Algorithm>(pageNo, pageSize, totalAlgorithmNumber);
        List<Algorithm> list = null;
        int prevNumber = (page.getPageNo() - 1) * pageSize;
        list = getForList(sql, keyword, prevNumber, pageSize);
        page.setItems(list);
        return page;
    }
}
