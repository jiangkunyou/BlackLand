package com.dlut.jky.app1.dao.impls;

import com.dlut.jky.app1.beans.Classifer;
import com.dlut.jky.app1.dao.inters.ClassiferDao;

import java.util.List;

/**
 * Created by jiangkunyou on 15/11/8.
 */
public class ClassiferDbutilsDaoImpl extends DbUtilsDaoImpl<Classifer> implements ClassiferDao {
    @Override
    public void insertClassifer(Classifer classifer) {
        String sql = "INSERT INTO classifer(className, classDescrib) VALUES(?, ?)";
        update(sql, classifer.getClassName(), classifer.getClassDescrib());
    }

    @Override
    public int insertClassiferAndReturnId(Classifer classifer) {
        String sql = "INSERT INTO classifer(className, classDescrib) VALUES(?, ?)";
        return insert(sql, classifer.getClassName(), classifer.getClassDescrib());
    }

    @Override
    public void deleteClassifer(Integer id) {
        String sql = "DELETE FROM classifer WHERE classId = ?";
        update(sql, id);
    }

    @Override
    public void updateClassifer(Classifer classifer) {
        String sql = "UPDATE classifer SET className = ?, classDescrib = ? WHERE classId = ?";
        update(sql, classifer.getClassName(), classifer.getClassDescrib(), classifer.getClassId());
    }

    @Override
    public Classifer getClassiferById(Integer id) {
        String sql = "SELECT * FROM classifer WHERE classId = ?";
        return get(sql, id);
    }

    @Override
    public List<Classifer> getClassifers(Object... args) {
        String sql = "SELECT * FROM classifer";
        return getForList(sql, args);
    }
}
