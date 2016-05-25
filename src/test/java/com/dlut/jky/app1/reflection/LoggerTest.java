package com.dlut.jky.app1.reflection;

import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 15/11/2.
 */
public class LoggerTest {
    @Test
    public void testLogger(){
        Logger.getGlobal().setLevel(Level.OFF); // 关闭日志
        Logger.getGlobal().info("aaaaaa");  // 打印日志
    }
}
