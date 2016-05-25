package com.dlut.jky.app1.module.action;

import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.FormGroup;
import com.dlut.jky.app1.Services.UserService;
import com.dlut.jky.app1.beans.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jiangkunyou on 15/12/15.
 */
public class UserInfoAction {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private UserService userService;

    public void doUpdate(@FormGroup("userInfo") User user, Navigator nav) {
        try {
            userService.updateUser(user);
            // 数据库更新完一定要刷新一下session,否则在关闭浏览器前session中还是老的数据
            request.getSession().setAttribute("user", user);
        } catch (Exception e) {
            // 抛到connectionValve处理异常,回滚事务
            nav.redirectTo("app1Link").withTarget("404.vm");
            throw new RuntimeException();
        }
    }

}
