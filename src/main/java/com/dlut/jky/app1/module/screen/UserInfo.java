package com.dlut.jky.app1.module.screen;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.dlut.jky.app1.Services.AlgorithmService;
import com.dlut.jky.app1.Services.UserService;
import com.dlut.jky.app1.beans.Algorithm;
import com.dlut.jky.app1.beans.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 15/11/30.
 */
public class UserInfo {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    public void execute(Context context, Navigator nav) {
        validatorUser(context, nav);
    }

    // 验证用户身份
    private void validatorUser(Context context, Navigator nav){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null){
            Logger.getGlobal().info("");
            nav.redirectTo("app1Link").withTarget("404.vm");
        } else {
            context.put("user", user);
        }
    }


}
