package com.dlut.jky.app1.dao.inters;

import com.dlut.jky.app1.beans.User;

import java.util.List;

/**
 * Created by jiangkunyou on 15/11/3.
 */
public interface UserDao {

    void insertUser(User user);

    void deleteUser(Integer id);

    void updateUser(User user);

    User getUserById(Integer id);

    User getUserByName(String name);

    List<User> getUsers(Object... args);
}
