package com.dlut.jky.app1.module.screen;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.dlut.jky.app1.Services.AlgorithmService;
import com.dlut.jky.app1.Services.ClassiferService;
import com.dlut.jky.app1.Services.UserService;
import com.dlut.jky.app1.beans.Algorithm;
import com.dlut.jky.app1.beans.Classifer;
import com.dlut.jky.app1.beans.Page;
import com.dlut.jky.app1.beans.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.w3c.dom.ls.LSInput;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 15/11/26.
 */
public class Home {

    @Autowired
    private UserService userService;

    @Autowired
    private ClassiferService classiferService;

    @Autowired
    private AlgorithmService algorithmService;

    @Autowired
    private HttpServletRequest request;

    public void execute(@Param("userId") String userId, @Param("pageNo") String pageNo, @Param("classId") String classId, Context context, Navigator nav) {
        validatorUser(userId, context, nav);
        injectClassifers(context);
//        injectAlgorithms(pageNo, "8", classId, context);
    }

    // 获取当前页对象
    private void injectAlgorithms(String pageNo, String pageSize, String classId, Context context){
        // 若url中没提供该参数,代表是第一页
        int pageNo_int = 1;
        // 指定一个固定值,目前设成5
        int pageSize_int = Integer.parseInt(pageSize);
        // 若url中没提供该参数,代表是全部类别
        int classId_int = -1;
        context.put("classId", "-1");
        if(pageNo != null){
            pageNo_int = Integer.parseInt(pageNo);
        }
        if(classId != null){
            classId_int = Integer.parseInt(classId);
            context.put("classId", classId);
        }
        Page<Algorithm> myPage = algorithmService.getPageByPageNoAndClassId(pageNo_int, pageSize_int, classId_int);
        context.put("myPage", myPage);
    }

    // 根据url参数,获取user对象
    private void injectUser(String userId, Context context, Navigator nav){
        try {
            User user = userService.getUserById(Integer.parseInt(userId));
            if(user != null) {
                request.getSession().setAttribute("user", user);
//                request.getSession().setAttribute("aaaa", "dfdasfsff");
                context.put("user", user);
            }
        } catch (Exception e) {
            Logger.getGlobal().info("");
            nav.redirectTo("app1Link").withTarget("404.vm");
        }
    }

    // 获取所有classifer的列表
    private void injectClassifers(Context context){
        List<Classifer> classifers = classiferService.getClassifers();
        context.put("classifers", classifers);
    }

    // 验证用户身份,若验证成功,则把用户信息放入session中
    private void  validatorUser(String userId, Context context, Navigator nav){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null){
            injectUser(userId, context, nav);
        } else {
            context.put("user", user);
        }
    }
}
