package com.dlut.jky.app1.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jiangkunyou on 15/10/27.
 * 用户类
 * 这里用lombok插件提供类所有属性的setter，getter方法以及构造函数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    // 用户id
    private int userId;

    // 用户姓名
    private String userName;

    // 用户密码
    private String userPass;

    // 是否是管理员
    private int isSuper;


}
