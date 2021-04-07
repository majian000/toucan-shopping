package com.toucan.shopping.common.page;


import lombok.Data;

import java.util.List;

@Data
public class PageInfo<E> {

    /**
     * 数据总数
     */
    private Long total;

    /**
     * 数据列表
     */
    private List<E> list;

    public PageInfo(List<E> list)
    {
        this.list=list;
    }
}
