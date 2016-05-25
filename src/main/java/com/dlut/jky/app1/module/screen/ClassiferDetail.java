package com.dlut.jky.app1.module.screen;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.dlut.jky.app1.Services.ClassiferService;
import com.dlut.jky.app1.beans.Algorithm;
import com.dlut.jky.app1.beans.Classifer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 15/11/30.
 */
public class ClassiferDetail {

    @Autowired
    private ClassiferService classiferService;

    public void execute(@Param("classId") String algorId, Context context, Navigator nav) {
        injectClassifer(algorId, context, nav);
    }

    // 根据id获取算法对象,并传给前端模板
    private void injectClassifer(String classId, Context context, Navigator nav){
        try {
            Classifer classifer = classiferService.getClassiferById(Integer.parseInt(classId));
            context.put("classifer", classifer);
        } catch (Exception e) {
            Logger.getGlobal().info("");
            nav.redirectTo("app1Link").withTarget("404.vm");
        }
    }

}
