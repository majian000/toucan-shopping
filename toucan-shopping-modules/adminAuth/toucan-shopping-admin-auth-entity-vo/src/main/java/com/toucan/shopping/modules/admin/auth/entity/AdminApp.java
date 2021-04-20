package com.toucan.shopping.modules.admin.auth.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户应用关联表
 */
@Data
public class AdminApp {
    private Long id;

    /**
     * 管理员ID
     */
    private String adminId;

    /**
     * 所属应用
     */
    private String appCode;

    /**
     * 创建时间
     */
    private Date createDate;


    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    /**
     * 创建人
     */
    private String createAdminId;


    /**
     * 启用状态 0:禁用 1启用
     */
    private Short enableStatus;


    /**
     * 保存状态,在批量保存的时候返回哪些保存失败了
     */
    public int success=0;

    /**
     * 状态消息 返回为什么失败
     */
    private String msg;

}
