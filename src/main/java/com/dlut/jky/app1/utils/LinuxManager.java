package com.dlut.jky.app1.utils;

import lombok.Setter;
import java.util.Properties;
import java.util.Stack;

/**
 * Created by jiangkunyou on 15/11/19.
 * 后台服务器登陆信息管理类, 单例类
 */
public class LinuxManager {

    /**
     * 主要包含三个属性, hostname, username, password,
     * 为了后期改动方便用了properties,在initServlet中初始化
     */
    @Setter
    private Properties properties;

    // 使用静态内部类来创建单例,具体原因见<<剑指offer>>35页
    private static class LinuxManagerHolder{
        static LinuxManager singleton;
        static {
            singleton = new LinuxManager();
            // 在spring中利用factory-method来实现单例化
            InitManager.initCommon(singleton, "linux-config.properties");
        }
    }

    private LinuxManager(){}

//    private static LinuxManager manager = new LinuxManager();

    public static LinuxManager getInstance(){
        return LinuxManagerHolder.singleton;
    }

    public String getProperty(String propertyName){
        return properties.getProperty(propertyName);
    }
}
