package com.dlut.jky.app1.module.action;

import com.alibaba.citrus.service.uribroker.URIBrokerService;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.citrus.turbine.dataresolver.FormField;
import com.alibaba.citrus.turbine.dataresolver.FormGroup;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.dlut.jky.app1.Services.UserService;
import com.dlut.jky.app1.beans.User;
import com.dlut.jky.app1.utils.SSHHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by jiangkunyou on 15/10/27.
 */
public class LoginAction {

    @Autowired
    private UserService userService;
    @Autowired
    private URIBrokerService uriBrokerService;

    public void doValidateUser(@FormGroup("login") User user, @FormField(name = "userName", group = "login") String userName2, Navigator nav, TurbineRunData runData) {
//        System.out.println("--------------thread: " + Thread.currentThread().getName() + " url: " + runData.getRequest().getServletPath() + " doValidateUser");
//        System.out.println("---------------------------------");
//        try {
//            SSHHelper.ssh("source /etc/profile;mahout org.apache.mahout.clustering.syntheticcontrol.kmeans.Job");
////            SSHHelper.ssh("pwd");
////            SSHHelper.ssh("hadoop fs -rmr /user/jky/output/*");
////            SSHHelper.ssh("mahout org.apache.mahout.clustering.syntheticcontrol.kmeans.Job");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("-----------------------------------------");

        String userName = user.getUserName();
        String password = user.getUserPass();
        User user1 = userService.getUserByName(userName);
        if(user1 == null){
            System.out.println("查无此人!");
            nav.redirectTo("app1Link").withTarget("404.vm");
        }
        else if(user1.getUserPass().equals(password)){
            System.out.println("登陆成功!");
            nav.redirectTo("app1Link").withTarget("home.vm").withParameter("userId", String.valueOf(user1.getUserId()));
        } else {
            System.out.println("密码错误!");
        }

//        UserDao userDao = DaoFactory.getInstance().getDao("userDaoType");
//        User user1 = userDao.getUserById(1);
//        System.out.println(user1);

//        request.getRequestDispatcher("").forward();
//        response.sendRedirect();
//        List<String> userNames = Arrays.asList("aaa", "bbb", "ccc");
//        String result = null;
//        if(userNames.contains(req.getParameter("username"))){
//            result = "<font color='red'>���û����Ѿ�ע��!</font>";
//        } else {
//            result = "<font color='green'>�û�����Ч!</font>";
//        }
//        resp.setContentType("text/html; charset=UTF-8");
//        resp.setCharacterEncoding("UTF-8");
//        resp.getWriter().print(result);
//        nav.redirectTo("app1Link").withTarget("form/welcome").withParameter("name", name);
    }
}
