package com.dlut.jky.app1.module.screen;

import com.dlut.jky.app1.Services.UserService;
import com.dlut.jky.app1.beans.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 15/11/30.
 * 更新用户信息的一些异步操作,如更改密码,更改权限,验证用户名是否重名
 */
public class UpdateUserInfo {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private UserService userService;

    public void doUpdateUserPass() throws IOException {
        String password = request.getParameter("userPass");
        User user = (User) request.getSession().getAttribute("user");
        try {
            user.setUserPass(password);
            userService.updateUser(user);
            // 数据库更新完一定要刷新一下session,否则在关闭浏览器前session中还是老的数据
            request.getSession().setAttribute("user", user);
            // 是ajax发来的请求,所以需要回传信息给js,用于判断是否更新成功
            response.getWriter().print("success");
        } catch (Exception e) {
            Logger.getGlobal().info("");
            response.getWriter().print("failed");
        }
    }

    // 验证是否重名
    public void doValidateUserName() throws IOException {
        String newUserName = request.getParameter("newUserName");
        User user = userService.getUserByName(newUserName);
        if(user != null){
            response.getWriter().print("failed");
        } else {
            response.getWriter().print("success");
        }
    }
}
