package com.dlut.jky.app1.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jiangkunyou on 15/12/18.
 * 算法运行时由linux终端传回的log
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyLog {
    // 日志内容
    private String myContent;
    // 日志长度
    private String len;
}
