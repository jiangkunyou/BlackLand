package com.dlut.jky.app1.threads;

import com.dlut.jky.app1.utils.ExcelAndLuceneUtils;
import java.io.File;
import java.util.concurrent.CountDownLatch;

/**
 * Created by jiangkunyou on 16/4/24.
 */
public class FileRunnabel implements Runnable{

    private File file;
    private CountDownLatch cdl;

    public FileRunnabel(File file, CountDownLatch cdl){
        this.file = file;
        this.cdl = cdl;
    }

    public void run() {
        try {
            System.out.println("=====" + Thread.currentThread().getName() + "===== file: " + file.getName());
            ExcelAndLuceneUtils.readExcelAndCreateIndex(file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cdl.countDown();
        }
    }
}
