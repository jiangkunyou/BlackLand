package com.dlut.jky.app1.module.screen;

import com.alibaba.citrus.turbine.dataresolver.Param;
import com.dlut.jky.app1.Services.AlgorithmService;
import com.dlut.jky.app1.beans.Keyword;
import com.dlut.jky.app1.utils.ReadFromFile;
import com.dlut.jky.app1.utils.VisualMapManager;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 15/12/16.
 */
public class Visualization {

    @Autowired
    private AlgorithmService algorithmService;

    @Autowired
    private HttpServletResponse response;

    // 记录是否是第一次访问
    private boolean flag;

    public void doDrawFileWordCloud(@Param("algorId") String algorId, @Param("fileName") String fileName){
        try {
            Map<String, String> map = readDocIndex();
            String fileKey = map.get(fileName);
            // 如果没找到对应文件
            if(fileKey == null){
                Keyword keyword = new Keyword("noFile", -1);
                List<Keyword> list = new ArrayList<Keyword>();
                list.add(keyword);
                trans2Json(list);
                return;
            }
            String filePath = "/Users/jiangkunyou/IdeaProjects/KingCloud/target/KingCloud-1.0-SNAPSHOT/WEB-INF/upload/output/mydata-matrix.txt";
            List<String> list = readFileByLineNumber(filePath, Integer.parseInt(fileKey) + 3);
            int topK = Math.min(20, list.size());
            findTop(list, 0, list.size() - 1, topK);
//            for(int i = 0; i < topK; i++){
//                System.out.println("*************************" + list.get(i));
//            }
            Map<String, String> wordMap = readDictionaryFile();
            matchKeyword(list.subList(0, topK - 1), wordMap);
        } catch (Exception e) {
            Logger.getGlobal().info("");
            e.printStackTrace();
        }
    }

    /**
     * LDA 每个主题中含有多少文章
     * 获取饼图所需数据
     */
    public void doDrawNormalPie(@Param("algorId") String algorId){

        String filePath = "/Users/jiangkunyou/IdeaProjects/KingCloud/target/KingCloud-1.0-SNAPSHOT/WEB-INF/upload/output/mydata-lda-documents.txt";
        Map<String, Integer> map = new HashMap<String, Integer>();
        readFileAndArrange(filePath, map);
        List<Keyword> list = tranMap2List(map);
//        for(Keyword keyword: list){
//            System.out.println("----------------*****" + keyword);
//        }
        trans2Json(list);
    }

    // LDA 读出每个文件属于各个主题的概率,找到最大的概率并把该文章分配到该主题
    private void readFileAndArrange(String fileName, Map<String, Integer> map){
        FileReader fr = null;
        BufferedReader br = null;
        String value = null;
        String[] values = null;
        int i = 1;
        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            br.readLine();
            br.readLine();
            while((value = br.readLine()) != null){
                if(value.startsWith("Count")){
                    continue;
                }
                values = splitToStrings(value);
                doArrange(values, map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 把文章分配到概率最大的主题
    private void doArrange(String[] values, Map<String, Integer> map){
        if(values == null){
            return;
        }
        double max = 0;
        String topicKey = null;
        try {
            for(int i = 0; i < values.length; i++) {
                String[] temp = values[i].split(":");
                double percent = Double.parseDouble(temp[1]);
                if (percent > max) {
                    max = percent;
                    topicKey = "Topic " + temp[0];
                }
            }
            Integer tempCount = map.get(topicKey);
            map.put(topicKey, tempCount == null? 1: ++tempCount);
        } catch (Exception e) {
            Logger.getGlobal().info("");
            e.printStackTrace();
        }
    }

    // 把map转换成list
    private List<Keyword> tranMap2List(Map<String, Integer> map){
        if(map == null){
            return null;
        }
        List<Keyword> list = new ArrayList<Keyword>();
        for(Map.Entry<String, Integer> entry: map.entrySet()){
            Keyword keyword = new Keyword(entry.getKey(), entry.getValue());
            list.add(keyword);
        }
        return list;
    }

    /**
     * LDA 每篇文章属于各个主题的概率
     * 获取饼图所需数据,因为文章数往往很大,只实现向前向后翻页不实用,
     * 所以对于饼图提供输入文件名,显示对应文件的数据
     * @param fileName: 文章名
     */
    public void doDrawPie(@Param("algorId") String algorId, @Param("fileName") String fileName, @Param("isFirst") String isFirst){

        flag = (isFirst != null? true: false);
        Map<String, String> map = readDocIndex();
        String fileKey = map.get(fileName);
        // 如果没找到对应文件
        if(fileKey == null){
            Keyword keyword = new Keyword("noFile", -1);
            List<Keyword> list = new ArrayList<Keyword>();
            list.add(keyword);
            trans2Json(list);
            return;
        }

        String filePath = "/Users/jiangkunyou/IdeaProjects/KingCloud/target/KingCloud-1.0-SNAPSHOT/WEB-INF/upload/output/mydata-lda-documents.txt";
        List<String> list = readFileByLineNumber(filePath, Integer.parseInt(fileKey) + 3);
        decodeList(list);
    }

    /**
     * 用于解析通过LDA的lda-documents文件生成的list并转化成json数组传回前端
     * @param list: LDA的lda-documents文件生成的list
     */
    private void decodeList(List<String> list){
        List<Keyword> ret = new ArrayList<Keyword>();
        for(String s: list){
            String[] temp = s.split(":");
            String content = "Topic " + temp[0];
            // 每篇文章属于各个主题的概率
            Keyword keyword = new Keyword(content, Double.parseDouble(temp[1]));
            ret.add(keyword);
        }
        trans2Json(ret);
    }

    /**
     * LDA 每个主题的关键字提取
     * 获取字符云所需参数
     * @param topicId: 主题id
     */
    public void doDrawWordCloud(@Param("algorId") String algorId, @Param("topicId") String topicId, @Param("isFirst") String isFirst){
        // 拿到每个主题对应的关键词概率文件, 形式是一行代表一个主题,key是主题,value是每个关键词属于该主题的概率
//        String filePath = FileUploadManager.getInstance().getOutputPath() + "/mydata-lda.txt";
        String filePath = "/Users/jiangkunyou/IdeaProjects/KingCloud/target/KingCloud-1.0-SNAPSHOT/WEB-INF/upload/output/mydata-lda.txt";
        flag = (isFirst != null? true: false);
        try {
            List<String> list = readFileByLineNumber(filePath, Integer.parseInt(topicId) + 2);
            // 目前设置上限是36,字符云看上去比较美观
            int topKey = Math.min(36, list.size());
            findTop(list, 0, list.size() - 1, topKey);
            Map<String, String> map = readDictionaryFile();
            matchKeyword(list.subList(0, topKey - 1), map);
        } catch (Exception e) {
            Logger.getGlobal().info("");
            e.printStackTrace();
        }
    }

    // 读取LDA算法生成的XXX-lda-documents和XXX-lda文件,并转化成list
    private List<String> readFileByLineNumber(String filePath, int lineNumber){
        String value = ReadFromFile.readFileByLineNumber(filePath, lineNumber);
        String[] values = splitToStrings(value);
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < values.length; i++){
            list.add(values[i]);
        }
        return list;
    }

    // 把一个长字符串根据相应规则拆分成字符数组
    private String[] splitToStrings(String value){
        if(value == null){
            return null;
        }
        String[] values = value.split(",");
        values[0] = values[0].substring(values[0].indexOf("{") + 1);
        values[values.length - 1] = values[values.length - 1].substring(0, values[values.length - 1].length() - 1);
        return values;
    }

    /**
     * 拿到排名最高的20个关键字的id后,需要join map,来获取关键字内容
     * @param list: 排名最高的20个关键字id和概率
     * @param map: 关键字id和关键字内容的映射
     */
    private void matchKeyword(List<String> list, Map<String, String> map){
        List<Keyword> ret = new ArrayList<Keyword>();
        for(String s: list){
            String[] temp = s.split(":");
            String content = map.get(temp[0]);
            Keyword keyword = new Keyword(content, Double.parseDouble(temp[1]));
            ret.add(keyword);
        }
        trans2Json(ret);
    }

    // 将keyword数组转成json传回前端
    private void trans2Json(List<Keyword> list){
        try {
            Gson gson = new Gson();
            String jsonStr = gson.toJson(list);
//            System.out.println("-------------" + jsonStr + "---------");
            response.getWriter().print(jsonStr);
        } catch (IOException e) {
            Logger.getGlobal().info("");
            e.printStackTrace();
        }
    }

    /**
     * 筛选20个概率最高的关键词(LDA算法使用),类似快排的思想
     * 注意这里的list里的每个string的形式如0:8.273437884022019E-10,
     * 需要取后面的数字进行比较,前面的0是key,用来查关键词
     * @param left: 起始坐标
     * @param right: 终止坐标
     * @param topKey: 查找排名前几的元素
     */
    private void findTop(List<String> list, int left, int right, int topKey){
        if(left < right){
            int middle = partition(list, left, right, topKey);
            if(middle + 1 < topKey){
                findTop(list, middle + 1, right, topKey - middle - 1);
            }
            else if(middle + 1 == topKey){
                return;
            }
            else{
                findTop(list, left, middle - 1, topKey);
            }
        }
    }

    /**
     * 类似快排的partition,辅助查找前20的元素
     */
    private int partition(List<String> list, int left, int right, int topKey){
        int index = left - 1;
        double pivot = Double.parseDouble(list.get(right).split(":")[1]);
        int L = left;
        while(L < right){
            double current = Double.parseDouble(list.get(L).split(":")[1]);
            if(current >= pivot){
                swap2ElementInList(list, ++index, L);
            }
            L++;
        }
        swap2ElementInList(list, ++index, right);
        return index;
    }

    // 交换list中指定位置的两个元素
    private void swap2ElementInList(List<String> list, int first, int second){
        String temp = list.get(first);
        list.set(first, list.get(second));
        list.set(second, temp);
    }

    // 读取LDA算法生成的关键字->标号的映射文件
    private Map<String, String> readDictionaryFile(){
        // 如果不是第一次访问,应该是通过点击页面的next或者previous发来的请求,这时候因为已经载入过一次map,所以直接获取
        if(!flag){
            return VisualMapManager.getInstance().getMap("dictionary-file");
        }
        String filePath = "/Users/jiangkunyou/IdeaProjects/KingCloud/target/KingCloud-1.0-SNAPSHOT/WEB-INF/upload/output/mydata-dictionary.txt";
        Map<String, String> map = ReadFromFile.readDictionary(filePath);
        VisualMapManager.getInstance().addMap("dictionary-file", map);
        return map;
    }

    // 读取LDA算法生成的docIndex文件
    private Map<String, String> readDocIndex(){
        // 如果不是第一次访问,应该是通过点击搜索文件发来的请求,这时候因为已经载入过一次map,所以直接获取
        if(!flag){
            return VisualMapManager.getInstance().getMap("docIndex");
        }
        String filePath = "/Users/jiangkunyou/IdeaProjects/KingCloud/target/KingCloud-1.0-SNAPSHOT/WEB-INF/upload/output/mydata-docIndex.txt";
        Map<String, String> map = ReadFromFile.readDictionary(filePath);
        VisualMapManager.getInstance().addMap("docIndex", map);
        return map;
    }



}
