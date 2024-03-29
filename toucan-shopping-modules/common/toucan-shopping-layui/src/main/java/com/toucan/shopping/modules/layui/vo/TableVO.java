package com.toucan.shopping.modules.layui.vo;

import lombok.Data;

import java.util.List;

/**
 * 表格对象
 */
@Data
public class TableVO<T> {

    public static int SUCCESS=0;
    public static int FAILD=1;

    private int code;
    private String msg;
    private Long count;
    private List<T> data;


    /**
     * elasticsearch分页相关
     */
    private Object[] sortValues;
}
