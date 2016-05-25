package com.dlut.jky.app1.beans;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 15/11/23.
 * 封装分页类
 */
public class Page<T> {

    // 当前页码
    @Setter private int pageNo;

    // 当前页中包含的元素集合
    @Setter @Getter private List<T> items;

    // 每页的元素个数
    @Setter @Getter private int pageSize;

    // 总共有多少元素,主要用于计算一共有多少页
    @Setter @Getter private long totalItemNumber;

    // 构造函数
    public Page(int pageNo, int pageSize, long totalItemNumber){
        super();
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalItemNumber = totalItemNumber;
    }

    // 获取当前页码,要验证一下正确性
    public int getPageNo(){
        if(pageNo < 0){
            pageNo = 1;
        }
        else if(pageNo > getTotalPageNo()){
            pageNo = (int) getTotalPageNo();
        }
        return pageNo;
    }

    // 获取总页数
    public int getTotalPageNo(){
        int totalPageNo = (int) (totalItemNumber / pageSize);
        if(totalItemNumber % pageSize == 0)
            return totalPageNo;
        return (totalPageNo + 1);
    }

    // 是否有下一页
    public boolean hasNext(){
        if(pageNo < getTotalPageNo())
            return true;
        return false;
    }

    // 是否有上一页
    public boolean hasPrev(){
        return pageNo == 1? false: true;
    }

    // 获得上一页的页码
    public int getPrevPageNo(){
        if(hasPrev()){
            return pageNo - 1;
        }
        return pageNo;
    }

    // 获得下一页的页码
    public int getNextPageNo(){
        if(hasNext())
            return pageNo + 1;
        return pageNo;
    }
}
