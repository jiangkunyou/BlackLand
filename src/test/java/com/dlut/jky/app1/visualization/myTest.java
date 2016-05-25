package com.dlut.jky.app1.visualization;

import com.dlut.jky.app1.module.screen.Visualization;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangkunyou on 15/12/31.
 */
public class myTest {

    @Test
    public void testTop20(){
        List<String> list = new ArrayList<String>();
        list.add("0:8.273437884022019E-10");
        list.add("1:2.449101912745882E-9");
        list.add("3:4.700553405305792E-9");
        list.add("6:2.4490725098015134E-9");
        list.add("7:1.3264440899495147E-9");
        list.add("8:2.920725225443008E-9");
        list.add("8:2.920725225443008E-9");
        list.add("8:2.920725225443008E-8");
        list.add("8:111");
        Visualization visualization = new Visualization();
//        visualization.top20(list, 0, list.size() - 1, 5);
        for(String s: list){
            System.out.println(s);
        }
    }

    @Test
    public void testDoCharCloud(){
        Visualization visualization = new Visualization();
//        visualization.doCharCloud(1);
    }

}
