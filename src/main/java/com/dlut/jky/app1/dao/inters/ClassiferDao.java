package com.dlut.jky.app1.dao.inters;

import com.dlut.jky.app1.beans.Classifer;

import java.util.List;

/**
 * Created by jiangkunyou on 15/11/8.
 */
public interface ClassiferDao {

    void insertClassifer(Classifer classifer);

    // 插入一个新的类别后,返回新类别的id
    int insertClassiferAndReturnId(Classifer classifer);

    void deleteClassifer(Integer id);

    void updateClassifer(Classifer classifer);

    Classifer getClassiferById(Integer id);

    List<Classifer> getClassifers(Object... args);
}
