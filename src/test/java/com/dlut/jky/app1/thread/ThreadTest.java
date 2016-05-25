package com.dlut.jky.app1.thread;


import com.dlut.jky.app1.beans.User;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jiangkunyou on 15/11/25.
 */
public class ThreadTest {

    @Test
    public void test1(){
//        myRunnable myRunnable = new myRunnable();
//        new Thread(myRunnable, "a").start();
//        new Thread(myRunnable, "b").start();
//        new Thread(myRunnable, "c").start();

        User user = new User(1,"aaa", "aaa", 1);
//        user.setUserId(1);
        User user2 = new User(1,"aaa", "aaa", 1);
        user2.setUserId(2);

        Set<User> users = new HashSet<User>();
        users.add(user);
//        user.setUserId(3);
//        users.add(user);
        users.add(user2);
        System.out.println(user.equals(user2));
        for(User temp: users){
            System.out.println(temp.hashCode());
        }


    }

}
