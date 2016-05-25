package com.dlut.jky.app1.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jiangkunyou on 15/11/8.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Classifer {

    // 算法类别编号
    private int classId;

    // 算法类别名称
    private String className;

    // 算法类别描述
    private String classDescrib;
}
