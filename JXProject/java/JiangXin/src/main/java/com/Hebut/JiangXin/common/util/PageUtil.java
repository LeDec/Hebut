package com.Hebut.JiangXin.common.util;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义类型分页
 *
 * @author lidong
 */
@Data
public class PageUtil<T> implements Serializable {

    private static final long serialVersionUID = -8741766802354222579L;

    private int size;

    private int current;

    private int total;

    private int pages;

    private List<T> records = new ArrayList<>();

    public PageUtil() {
    }

    public PageUtil(int pageSize, int currentPage, int totalRecord, int totalPage, List<T> dataList) {
        super();
        this.size = pageSize;
        this.current = currentPage;
        this.total = totalRecord;
        this.pages = totalPage;
        this.records = dataList;
    }

    public PageUtil(int current, int size, List<T> sourceList) {
        if (sourceList == null || sourceList.isEmpty()) {
            return;
        }

        // 总记录条数
        this.total = sourceList.size();

        // 每页显示多少条记录
        this.size = size;

        // 获取总页数
        this.pages = this.total / this.size;
        if (this.total % this.size != 0) {
            this.pages = this.pages + 1;
        }

        // 当前第几页数据
        this.current = Math.min(this.pages, current);

        // 起始索引
        int fromIndex = this.size * (this.current - 1);

        // 结束索引
        int toIndex = Math.min(this.size * this.current, this.total);
        this.records = sourceList.subList(fromIndex, toIndex);
    }
}
