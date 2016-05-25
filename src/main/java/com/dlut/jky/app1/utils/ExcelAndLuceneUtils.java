package com.dlut.jky.app1.utils;

import com.dlut.jky.app1.threads.FileRunnabel;
import com.dlut.jky.app1.threads.SheetRunnable;
import com.hankcs.lucene.HanLPAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 16/4/24.
 */
public class ExcelAndLuceneUtils {

    public static ExecutorService fixedPool = Executors.newFixedThreadPool(10);

    public static List<File> getAllExcelFiles(File file){
        if(file == null)
            return null;
        List<File> files = new ArrayList<File>();
        if(file.isFile()){
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf('.') + 1);
            if("xls".equals(suffix) || "xlsx".equals(suffix)){
                files.add(file);
            }
        } else {
            File[] tempFiles = file.listFiles();
            for(File file1: tempFiles){
                files.addAll(getAllExcelFiles(file1));
            }
        }
        return files;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     */
    public static void deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                deleteDir(new File(dir, children[i]));
            }
        }
        // 目录此时为空，可以删除
        dir.delete();
    }

    //使用POI读入excel并利用lucene建索引
    public static void readExcelAndCreateIndex(File file) {
        InputStream is = null;
        Workbook wb = null;
        try {
            is = new FileInputStream(file.getPath());
            //根据上述创建的输入流 创建工作簿对象
            wb = WorkbookFactory.create(is);
            int sheetCounts = wb.getNumberOfSheets();

            if(sheetCounts > 1){
                // 设置主线程等待线程池
                CountDownLatch cdl = new CountDownLatch(sheetCounts);
                for(int i = 0; i < sheetCounts; i++){
                    Sheet sheet = wb.getSheetAt(i);
                    fixedPool.execute(new SheetRunnable(sheet, file, cdl));
                }
                // 这里需要注意,当用多线程时,必须等待所有sheet都填充完才能执行后面那条write
                cdl.await();
            } else if(sheetCounts == 1) {
                createIndexAndFillSheet(wb.getSheetAt(0), file);
            } else {
                return;
            }
            wb.write(new FileOutputStream(file.getPath()));
        } catch (Exception e){
            Logger.getGlobal().info("");
            e.printStackTrace();
        } finally {
            try {
                wb.close();
                //关闭输入流
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void createIndexAndFillSheet(Sheet sheet, File file1) {

        Map<Integer, Integer> lastCount = new HashMap<Integer, Integer>();
        // 索引文件夹
        String fileName = file1.getName().substring(0,  file1.getName().lastIndexOf('.'));
        File file = new File(file1.getParent() + "/" + fileName + "/" + sheet.getSheetName() + "_index");

        // 获取sheet的总行数
        int rowsSum = sheet.getLastRowNum();
        // 获取表的宽度,也就是列数(第一行是表头,以表头的属性数为准)
        int cellsSum = sheet.getRow(0).getPhysicalNumberOfCells();

        Directory directory = null;
        IndexWriter writer = null;
        Analyzer analyzer = null;
        try {
            directory = new SimpleFSDirectory(file.toPath());
            analyzer = new HanLPAnalyzer(true);
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            writer = new IndexWriter(directory, config);

            for (Row row: sheet) {
                if(row.getRowNum() != 0){
                    Document document = new Document();
                    String unique = createUniqueField(row, cellsSum, lastCount);
                    document.add(new TextField("unique", unique, TextField.Store.YES));
                    writer.addDocument(document);
                }
            }
        } catch (IOException e){
            Logger.getGlobal().info("");
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 过滤掉缺失比例太大的属性(比如sheet中所有元组的某一列属性值基本都缺失,那就不在填充)
        for(Map.Entry<Integer, Integer> entry: lastCount.entrySet()){
            // 过滤后map中只保留缺失比例小, 只是部分元组缺失,大部分其他元组都完整的属性
            if(entry.getValue() * 1.0 / rowsSum >= 0.5){
                lastCount.put(entry.getKey(), 0);
            }
        }

        // 填充该sheet
        try {
            fillLastValue(sheet, lastCount, directory, analyzer);

        } catch (ParseException e) {
            Logger.getGlobal().info("");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                directory.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 填充某一个sheet
    private static void fillLastValue(Sheet sheet, Map<Integer, Integer> lastCount, Directory directory, Analyzer analyzer) throws IOException, ParseException {
        if(lastCount == null || lastCount.size() <= 0){
            return;
        }
        // 获取表的宽度,也就是列数(第一行是表头,以表头的属性数为准)
        int cellsSum = sheet.getRow(0).getPhysicalNumberOfCells();
        for (Row row: sheet) {
            String queryString = null;
            List<Document> top10 = null;
            for (int i = 0; i < cellsSum; i++) {
                Cell cell = row.getCell(i);
                if(cell == null || "".equals(cell.toString().trim())){
                    // 属性缺失,并且其他元组可以提供有力支持,这时需要填充
                    if(lastCount.containsKey(i) && lastCount.get(i) > 0){
                        if(queryString == null){
                            queryString = createUniqueField(row, cellsSum, null);
                        }
                        if(top10 == null){
                            top10 = createTop10(directory, analyzer, queryString);
                            System.out.println("************************");
                            for(Document document: top10){
                                System.out.println("row: " + row.getRowNum() + " col: " + i + " doc: " + document.getField("unique").stringValue());
                            }
                            System.out.println("************************");
                        }
                        doFill(top10, sheet, row.getRowNum(), i);
                    }
                }
            }
        }
    }

    /**
     * 根据关键字进行检索, 返回最关联的top10 Documents
     */
    private static ArrayList<Document> createTop10(Directory directory, Analyzer analyzer, String queryString) throws IOException, ParseException {

        DirectoryReader iReader = null;
        ArrayList<Document> documents = null;
        try {
            iReader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(iReader);
            QueryParser queryParser = new QueryParser("unique", analyzer);
            queryParser.setDefaultOperator(QueryParser.Operator.OR);
            queryParser.setLowercaseExpandedTerms(true);
            Query query = queryParser.parse(queryString);
            documents = new ArrayList<Document>();
            TopDocs td = null;
            td = searcher.search(query, 11);
            for (int i = 1; i < td.scoreDocs.length; i++) {
                Document firstHit = null;
                firstHit = searcher.doc(td.scoreDocs[i].doc);
                documents.add(firstHit);
            }
        } catch (IOException e){
            Logger.getGlobal().info("");
            e.printStackTrace();
        } finally {
            iReader.close();
        }

        return documents;
    }

    // 填充具体cell
    private static void doFill(List<Document> top10, Sheet sheet, int x, int y){
        for(Document document: top10){
            // 把拼接的unique串拆成单元格
            String[] stringCells = document.getField("unique").stringValue().split("\t");
            if(!"#".equals(stringCells[y])){
                updateCell(sheet, x, y, stringCells[y]);
                return;
            }
        }
        updateCell(sheet, x, y, "sorry, we can't fill the value.");
    }

    // 读取sheet中的一行,并拼接成一个string
    private static String createUniqueField(Row row, int cellsSum, Map<Integer, Integer> lastCount){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cellsSum; i++) {
            Cell cell = row.getCell(i);
            if(cell == null || "".equals(cell.toString().trim())){
                if(lastCount != null){
                    lastCount.put(i, lastCount.containsKey(i)? lastCount.get(i) + 1: 1);
                }
                if(i == cellsSum - 1){
                    builder.append('#');
                } else {
                    builder.append("#\t");
                }
            } else {
                if(i == cellsSum - 1){
                    builder.append(cell.toString());
                } else {
                    builder.append(cell.toString() + "\t");
                }
            }
        }
        return builder.toString();
    }

    // 修改某个cell值
    public static void updateCell(Sheet sheet, int x, int y, String value){
        Cell cell = sheet.getRow(x).getCell(y);
        if(cell == null){
            sheet.getRow(x).createCell(y).setCellValue(value);
            return;
        }
        switch (cell.getCellType()){
            // 数字
            case 0:
                double valueInt = Double.parseDouble(value);
                cell.setCellValue(valueInt);
                break;
            // 字符串
            case 1:
                cell.setCellValue(value);
                break;
            default:
                cell.setCellValue(value);
        }
    }

    public static void myGod(String filePath) throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
//        fixedPool = Executors.newFixedThreadPool(10);
        List<File> files = null;
        File file = new File(filePath);
        // 不知道为何,这里如果不等待一秒,就出问题
        Thread.sleep(1000);
        files = getAllExcelFiles(file);
        if(files == null){
            return;
        }
        int fileSize = files.size();
        if(fileSize == 1){
            readExcelAndCreateIndex(files.get(0));
        } else if(fileSize > 1){
            CountDownLatch cdl = new CountDownLatch(fileSize);
            for(File file1: files){
                fixedPool.execute(new FileRunnabel(file1, cdl));
            }
//            fixedPool.shutdown();
            cdl.await();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("总共花费" + (endTime - startTime) + "毫秒.");
    }

}
