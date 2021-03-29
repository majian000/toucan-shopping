package com.toucan.shopping.center.app.export.entity;

import lombok.Data;

/**
 * 应用列表查询页
 */
@Data
public class AppPageInfo {

    /**
     * 页码
     */
    private int page;

    /**
     * 每页显示数量
     */
    private int size;

    /**
     * 每页显示数量
     */
    private int limit;

    /**
     * 所属管理员账号ID
     */
    private String adminId;


    /**
     * 启用状态 0:禁用 1启用
     */
    private Short enableStatus;


    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用编码
     */
    private String code;

}
