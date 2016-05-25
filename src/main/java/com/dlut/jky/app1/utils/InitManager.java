package com.dlut.jky.app1.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 15/11/25.
 * 改类负责初始化各种单例类的properties属性
 */
public class InitManager {

    /**
     * 因为很多单例对象中的map属性的初始化操作都一样,所以单独抽象出来一个方法
     * @param instance: 单例对象
     * @param fileName: 配置文件名字
     */
     public static <T> void initCommon(T instance, String fileName){
        // 获得当前类路径下的资源文件
        InputStream in = InitManager.class.getClassLoader().getResourceAsStream(fileName);
        Properties properties = new Properties();
        Method method = null;
        try {
            properties.load(in);
            // 单例类中都有setProperties方法
            method = instance.getClass().getDeclaredMethod("setProperties", Properties.class);
            method.invoke(instance, properties);

        } catch (IllegalAccessException e) {
            Logger.getGlobal().info(instance.getClass().getName() + " error!");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Logger.getGlobal().info(instance.getClass().getName() + " error!");
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            Logger.getGlobal().info(instance.getClass().getName() + " error!");
            e.printStackTrace();
        } catch (IOException e) {
            Logger.getGlobal().info(instance.getClass().getName() + " error!");
            e.printStackTrace();
        }
    }
}
