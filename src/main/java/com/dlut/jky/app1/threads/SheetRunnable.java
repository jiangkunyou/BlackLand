package com.dlut.jky.app1.threads;

import com.dlut.jky.app1.utils.ExcelAndLuceneUtils;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 16/4/24.
 */
public class SheetRunnable implements Runnable {

    private Sheet sheet;
    private File file;
    private CountDownLatch cdl;

    public SheetRunnable(Sheet sheet, File file, CountDownLatch cdl){
        this.sheet = sheet;
        this.file = file;
        this.cdl = cdl;
    }

    public void run() {
        try {
            System.out.println("=======" + Thread.currentThread().getName() + "=======: sheet: " + sheet.getSheetName());
            ExcelAndLuceneUtils.createIndexAndFillSheet(sheet, file);
        } catch (Exception e){
            Logger.getGlobal().info("");
            e.printStackTrace();
        } finally {
            cdl.countDown();
        }
    }
}
