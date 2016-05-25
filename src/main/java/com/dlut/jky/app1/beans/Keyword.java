package com.dlut.jky.app1.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jiangkunyou on 15/12/31.
 * LDA算法中关键字对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Keyword {
    // 关键字内容
    private String content;
    // 关键字概率
    private double percent;
}
