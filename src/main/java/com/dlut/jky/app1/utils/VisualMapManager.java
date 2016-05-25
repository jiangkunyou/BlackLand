package com.dlut.jky.app1.utils;

import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiangkunyou on 16/1/2.
 * 管理可视化部分需要的映射文件, 单例类
 */
public class VisualMapManager {

    private Map<String, Map<String, String>> allMaps;

    // 使用静态内部类来创建单例,具体原因见<<剑指offer>>35页
    private static class VisualMapManagerHolder{
        static VisualMapManager singleton;
        static {
            singleton = new VisualMapManager();
        }
    }

    private VisualMapManager(){
        allMaps = new HashMap<String, Map<String, String>>();
    }

//    private static LinuxManager manager = new LinuxManager();

    public static VisualMapManager getInstance(){
        return VisualMapManagerHolder.singleton;
    }

    // 添加map进allMaps
    public void addMap(String mapName, Map<String, String> map){
        allMaps.put(mapName, map);
    }

    // 获取某个指定map
    public Map<String, String> getMap(String mapName){
        return allMaps.get(mapName);
    }
}
