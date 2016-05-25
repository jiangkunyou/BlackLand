package com.dlut.jky.app1.Services;

import com.dlut.jky.app1.beans.User;
import com.dlut.jky.app1.dao.factories.DaoFactory;
import com.dlut.jky.app1.dao.inters.UserDao;
import lombok.NoArgsConstructor;

/**
 * Created by jiangkunyou on 15/11/23.
 */
@NoArgsConstructor
public class UserService {

    // 获取userDao实现类对象
    private UserDao userDao = DaoFactory.getInstance().getDao("userDaoType");

    // 根据用户名获取用户对象
    public User getUserByName(String name){
        return userDao.getUserByName(name);
    }

    public User getUserById(Integer id) {
        return userDao.getUserById(id);
    }

    public void updateUser(User user) {
        userDao.updateUser(user);
    }
}
