package com.dlut.jky.app1.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by jiangkunyou on 15/10/29.
 * 算法类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Algorithm {
    // 算法id
    private int algorId;

    // 算法名称
    private String algorName;

    // 所属类别编号
    private int classId;

    // 算法作者
    private String algorAuthor;

    // 算法修改日期
    private Date algorModify;

    // 算法简介
    private String algorDescrib;

    // 算法使用说明
    private String algorCareful;

    // 算法命令
    private String algorCommand;

    // 算法所属类别名
    private String className;
}
