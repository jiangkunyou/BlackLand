package com.dlut.jky.app1.Services;

import com.dlut.jky.app1.beans.Classifer;
import com.dlut.jky.app1.dao.factories.DaoFactory;
import com.dlut.jky.app1.dao.inters.ClassiferDao;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by jiangkunyou on 15/11/23.
 */
@NoArgsConstructor
public class ClassiferService {

    private ClassiferDao classiferDao = DaoFactory.getInstance().getDao("classiferDaoType");

    public List<Classifer> getClassifers(){
        return classiferDao.getClassifers();
    }

    public Classifer getClassiferById(Integer id){
        return classiferDao.getClassiferById(id);
    }

    public void insertClassifer(Classifer classifer) {
        classiferDao.insertClassifer(classifer);
    }

//    插入新类别并返回类别id
    public int insertClassiferAndReturnId(Classifer classifer) {
       return classiferDao.insertClassiferAndReturnId(classifer);
    }
}
