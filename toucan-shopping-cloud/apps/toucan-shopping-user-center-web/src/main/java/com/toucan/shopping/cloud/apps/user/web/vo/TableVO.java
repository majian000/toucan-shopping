package com.toucan.shopping.cloud.apps.user.web.vo;

import lombok.Data;

import java.util.List;

/**
 * 表格对象
 */
@Data
public class TableVO {

    public static int SUCCESS=0;
    public static int FAILD=1;

    private int code;
    private String msg;
    private Long count;
    private List<Object> data;


    /**
     * elasticsearch分页相关
     */
    private Object[] sortValues;
}
