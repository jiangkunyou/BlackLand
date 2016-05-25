package com.dlut.jky.app1.factories;

import com.dlut.jky.app1.beans.User;
import com.dlut.jky.app1.dao.factories.DaoFactory;
import com.dlut.jky.app1.dao.inters.UserDao;
import org.junit.Test;

/**
 * Created by jiangkunyou on 15/11/5.
 */
public class FactoriesTest {
    @Test
    public void test1(){
        UserDao userDao = DaoFactory.getInstance().getDao("userDaoType");
        User user = userDao.getUserById(1);
        System.out.println(user);
    }
}
