package com.dlut.jky.app1.dao.factories;

import com.dlut.jky.app1.utils.InitManager;
import lombok.Setter;

import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 15/11/4.
 * 单例工厂类
 */
public class DaoFactory {

    // 私有化构造函数,禁止类外调用,实现单例
    private DaoFactory(){}

    // 使用静态内部类来创建单例,具体原因见<<剑指offer>>35页
    private static class DaoFactoryHolder{
        static DaoFactory singleton;
        static {
            singleton = new DaoFactory();
            // 因为在userService等类中要调用daoFactory的properties属性,所以必须在那些类加载前完成该类的初始化
            // 在spring中利用factory-method来实现单例化
            InitManager.initCommon(singleton, "daoType.properties");
        }
    }

    @Setter private Properties properties;

    public static DaoFactory getInstance(){
        return DaoFactoryHolder.singleton;
    }

    /**
     * 获得userDao的实例对象
     * @param typeName: 传入需要创建的dao类型的key, 如userDaoType, 对应的value是要创建对象的全类名
     */
    public <T> T getDao(String typeName){
        String className = properties.getProperty(typeName);
        try {
            Class clazz = Class.forName(className);
            return (T) clazz.newInstance();
        } catch (Exception e) {
            Logger.getGlobal().info("getDao error!");
            e.printStackTrace();
        }
        return null;
    }

}
