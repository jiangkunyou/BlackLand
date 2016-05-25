package com.dlut.jky.app1.servlets;

import com.dlut.jky.app1.utils.FileUploadManager;
import com.dlut.jky.app1.utils.InitManager;
import com.dlut.jky.app1.utils.LinuxManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by jiangkunyou on 15/11/5.
 * 该servlet只在web应用加载时初始化一次, 因此在这里进行一些项目的初始化设置
 */
public class InitServlet extends HttpServlet{

    @Override
    public void init() throws ServletException {
//        initDaoType();  // daoFactory的初始化工作放在其类内部进行
//        initFileUploadProperties();
//        initLinuxManager();
    }

    /**
     * 初始化后台服务器登陆信息配置文件
     */
    private void initLinuxManager(){
        InitManager.initCommon(LinuxManager.getInstance(), "linux-config.properties");
//        System.out.println("------------" + LinuxManager.getInstance().getProperty("hostname"));
    }

    /**
     * 初始化文件上传属性的配置文件
     */
    private void initFileUploadProperties(){
        InitManager.initCommon(FileUploadManager.getInstance(), "upload-config.properties");
//        System.out.println("------------" + FileUploadManager.getInstance().getProperty("singlefileMaxSize"));
    }

    /**
     * 初始化daoType配置文件
     */
//    private void initDaoType(){
//        InitManager.initCommon(DaoFactory.getInstance(), "daoType.properties");
////        System.out.println("--------" + DaoFactory.getInstance().getDao("userDaoType"));
//    }

    /**
     * 因为很多单例对象中的map属性的初始化操作都一样,所以单独抽象出来一个方法
     * @param instance: 单例对象
     * @param fileName: 配置文件名字
     */
//    private <T> void initCommon(T instance, String fileName){
//        // 获得当前类路径下的资源文件
//        InputStream in = getClass().getClassLoader().getResourceAsStream(fileName);
//        Properties properties = new Properties();
//        Method method = null;
//        try {
//            properties.load(in);
//            // 单例类中都有setProperties方法
//            method = instance.getClass().getDeclaredMethod("setProperties", Properties.class);
//            method.invoke(instance, properties);
//
//        } catch (IllegalAccessException e) {
//            Logger.getGlobal().info(instance.getClass().getName() + " error!");
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            Logger.getGlobal().info(instance.getClass().getName() + " error!");
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            Logger.getGlobal().info(instance.getClass().getName() + " error!");
//            e.printStackTrace();
//        } catch (IOException e) {
//            Logger.getGlobal().info(instance.getClass().getName() + " error!");
//            e.printStackTrace();
//        }
//    }
}
