package com.dlut.jky.app1.dao.impls;

import com.dlut.jky.app1.beans.User;
import com.dlut.jky.app1.dao.inters.UserDao;

import java.util.List;

/**
 * Created by jiangkunyou on 15/10/31.
 * user对象运用DbUtils工具实现的DAO
 */
public class UserDbUtilsDaoImpl extends DbUtilsDaoImpl<User> implements UserDao {

    @Override
    public void insertUser(User user) {
        String sql = "INSERT INTO user(userId, userName, userPass, isSuper) VALUES(?, ?, ?, ?)";
        update(sql, user.getUserId(), user.getUserName(), user.getUserPass(), user.getIsSuper());
    }

    @Override
    public void deleteUser(Integer id) {
        String sql = "DELETE FROM user WHERE userId = ?";
        update(sql, id);
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE user SET userName = ?, userPass = ?, isSuper = ? WHERE userId = ?";
        update(sql, user.getUserName(), user.getUserPass(), user.getIsSuper(), user.getUserId());
    }

    @Override
    public User getUserById(Integer id) {
        String sql = "SELECT * FROM user WHERE userId = ?";
        User user = get(sql, id);
        return user;
    }

    @Override
    public User getUserByName(String name) {
        String sql = "SELECT * FROM user WHERE userName = ?";
        User user = get(sql, name);
        return user;
    }

    @Override
    public List<User> getUsers(Object... args) {
        String sql = "SELECT * FROM user";
        List<User> users = getForList(sql, args);
        return users;
    }
}
