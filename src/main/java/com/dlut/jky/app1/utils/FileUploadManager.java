package com.dlut.jky.app1.utils;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.fileupload.FileItem;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 15/11/19.
 * 管理上传文件属性的工具类, 单例
 */
public class FileUploadManager {

    // 属性文件 在initServlet中初始化
    @Setter private Properties properties;

    // 上传文件保存到服务器时的文件名(前面加入随机数前缀 防止重名)
    private String finalFileName;

    // 上传文件的绝对路径(包含文件名)
    @Getter private String finalFilePath;

    // 算法运行时从linux终端传回的日志存放在服务器本地地址,就是upload文件夹的绝对路径下的logs文件夹
    @Getter private String logPath;
    // 算法运行时从linux终端传回的output存放在服务器本地地址,就是upload文件夹的绝对路径下的output文件夹
    @Getter private String outputPath;

    // 土地数据填充完以后,的存放地址(文件夹)
    @Setter @Getter String blackLandOutputPath;


    // 单例
//    private static FileUploadManager instance = new FileUploadManager();

    // 私有化构造函数 实现单例模式
    private FileUploadManager(){}

    // 使用静态内部类来创建单例,具体原因见<<剑指offer>>35页
    private static class FileUploadManagerHolder{
        static FileUploadManager singleton;
        static {
            singleton = new FileUploadManager();
            // 在spring中利用factory-method来实现单例化
            InitManager.initCommon(singleton, "upload-config.properties");
        }
    }

    // 获取单例对象
    public static FileUploadManager getInstance(){
        return FileUploadManagerHolder.singleton;
    }

    public String getProperty(String propertyName){
        return properties.getProperty(propertyName);
    }

    /**
     * 把上传到服务器的文件放入hdfs中,并删除已有输入文件和输出文件
     * 在使用算法的前提是集群的开启
     * 将文件上传到HDFS
     * 本机虚拟机用户为jk
     * 本机服务器应搭建在Linux系统上，所以finalFilePath应为linux下的文件全路径
     * 所以现阶段无法使用测试，暂用其他代替进行测试, 暂时需要先把文件通过scp传到虚拟机,然后再放入hdfs中
     */
    private void putIntoHDFS(String algorId){

        try {
            // 连接本地的机器 进行scp
//            System.out.println("---------------------" + finalFilePath + "--------------");
            if(algorId.equals("1")){
                SSHHelper.ssh("scp 192.168.1.100:" + finalFilePath + " /home/jky/");
//            System.out.println("---------------------" + finalFileName + "---------");
                // 去掉前缀和后缀的原始文件名
                String originalName = finalFileName.substring(finalFileName.indexOf('_') + 1, finalFileName.length() - 4);
                SSHHelper.ssh("source /etc/profile;unzip -o " + finalFileName + ";hadoop fs -rmr /user/jky/testdata/*;hadoop fs -put /home/jky/"+ originalName + "/* /user/jky/testdata/;hadoop fs -rmr /user/jky/output/*");
//            SSHHelper.ssh("source /etc/profile;hadoop fs -rmr /user/jky/output/*");
            }
            else{

            }

        } catch (Exception e) {
            Logger.getGlobal().info("");
            e.printStackTrace();
        }
    }

//    private void putIntoHDFS(String algorId){
//        try {
//            SSHHelper.ssh("scp 192.168.1.100:" + finalFilePath + " /home/jky/");
//            // 去掉前缀和后缀的原始文件名
//            int index = finalFileName.indexOf('_') + 1;
//            int length = finalFileName.length() - 4;
//            String originalName = finalFileName.substring(index, length);
//            SSHHelper.ssh("source /etc/profile;unzip -o " +
//                           finalFileName + ";" +
//                          "hadoop fs -rmr /user/jky/testdata/*;hadoop fs -put /home/jky/"+
//                           originalName +
//                          "/* /user/jky/testdata/;hadoop fs -rmr /user/jky/output/*");
//        } catch (Exception e) {
//            Logger.getGlobal().info("");
//            e.printStackTrace();
//        }
//    }

    // 这是真实环境应该使用的版本  上面那个是测试环境用的
//    private void putIntoHDFS(){
//
//        try {
//            SSHHelper.ssh("source /etc/profile;hadoop fs -put "+ finalFilePath + " /user/jky/testdata/");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    // 文件上传所有操作
    public void doFileUpload(List<FileItem> items, String fileSavePath, String algorId){
        logPath = fileSavePath + "/logs";
        outputPath = fileSavePath + "/output";
        for(FileItem item: items){
            // 如果fileitem中封装的是普通输入项的数据
            if(item.isFormField()){
                String name = item.getFieldName();
                // 解决普通输入项的数据的中文乱码问题
                try {
                    String value = item.getString("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Logger.getGlobal().info("");
                    e.printStackTrace();
                }
            }
            else{
                // 创建新文件名, 加入随机数前缀, 防重名覆盖
                if(makeFileName(item.getName()) == null){
                    continue;
                }
                // 创建文件保存全路径(包括文件名)
                makeFileDir(finalFileName, fileSavePath);

                // 上传文件到服务器
                try {
                    uploadFile(finalFilePath, item.getInputStream());
                } catch (IOException e) {
                    Logger.getGlobal().info("");
                    e.printStackTrace();
                }

                // 上传到HDFS
//                putIntoHDFS(algorId);

                //删除处理文件上传时生成的临时文件
                item.delete();

            }
        }
    }

    /**
     * 为上传文件重新命名,加入随机数前缀防重名
     * @param filename: 用户传上来的原文件名
     */
    private String makeFileName(String filename) {
        if (filename == null || filename.trim().equals("")) {
            return null;
        }
        //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
        //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
        filename = filename.substring(filename.lastIndexOf("/") + 1);
        finalFileName = UUID.randomUUID().toString() + "_" + filename;
        return finalFileName;
    }


    /**
     * 根据文件名的哈希码来创建upload目录下的多级子目录(目前创建两级,也就是最多256的子目录)
     * @param filename: 前面生成的新文件名
     * @param savePath: 文件上传根路径 /WEB-INF/upload
     */
    private void makeFileDir(String filename, String savePath) {
        // 得到文件名的hashCode的值，得到的就是filename这个字符串对象在内存中的地址
        int hashcode = filename.hashCode();
        // 取后四位 作为一级路径
        int dir1 = hashcode & 0x000f;
        // 取5~8位  作为二级路径
        int dir2 = (hashcode & 0x00f0) >> 4;

        // 创建最终的存储目录
        String dir = null;
        String type = filename.substring(filename.lastIndexOf(".") + 1);
        // 根据后缀名,判断是jar还是txt,分成不同的目录
        if(type.equals("jar")){
            dir = savePath + "/jars/" + dir1 + "/" + dir2 + "/";  // upload/2/3  upload/3/5
        } else {
            if(savePath.endsWith("/")){
                dir = savePath + "files/" + dir1 + "/" + dir2 + "/";  // upload/2/3  upload/3/5
            } else {
                dir = savePath + "/files/" + dir1 + "/" + dir2 + "/";  // upload/2/3  upload/3/5
            }
        }

        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        finalFilePath = dir + filename;
    }

    /**
     * 具体上传操作
     * @param path: 上传位置的绝对路径
     * @param in: item.getInputStream 文件的输入流
     */
    private void uploadFile(String path, InputStream in){
        if(in == null){
            return;
        }
        int len = 0;
        byte [] buffer = new byte[1024];
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            while((len = in.read(buffer)) != -1){
                fos.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            Logger.getGlobal().info("");
            e.printStackTrace();
        } catch (IOException e) {
            Logger.getGlobal().info("");
            e.printStackTrace();
        } finally {
            closeAll(in, fos);
        }
    }

    private void closeAll(InputStream in, FileOutputStream fos){
        if(fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                Logger.getGlobal().info("");
                e.printStackTrace();
            }
        }
        if(in != null){
            try {
                in.close();
            } catch (IOException e) {
                Logger.getGlobal().info("");
                e.printStackTrace();
            }
        }
    }
}
