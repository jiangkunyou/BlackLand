package com.dlut.jky.app1.Services;

import com.dlut.jky.app1.beans.Algorithm;
import com.dlut.jky.app1.beans.Page;
import com.dlut.jky.app1.dao.factories.DaoFactory;
import com.dlut.jky.app1.dao.inters.AlgorithmDao;
import lombok.NoArgsConstructor;

/**
 * Created by jiangkunyou on 15/11/23.
 */
@NoArgsConstructor
public class AlgorithmService {

    // 获取algorithmDao实现对象
    private AlgorithmDao algorithmDao = DaoFactory.getInstance().getDao("algorithmDaoType");

    // 获取当前页page对象
    public Page<Algorithm> getPageByPageNoAndClassId(int pageNo, int pageSize, int classId){
        return algorithmDao.getPageByPageNoAndClassiferId(pageNo, pageSize, classId);
    }

    // 根据id获取算法对象
    public Algorithm getAlgorithmById(int algorId) {
        return algorithmDao.getAlgorithmById(algorId);
    }

    public void insertAlgorithm(Algorithm algorithm) {
        algorithmDao.insertAlgorithm(algorithm);
    }
}
