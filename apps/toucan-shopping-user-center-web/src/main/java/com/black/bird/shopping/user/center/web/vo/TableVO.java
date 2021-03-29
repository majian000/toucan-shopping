package com.toucan.shopping.user.center.web.vo;

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
    private Integer count;
    private List<Object> data;
}
