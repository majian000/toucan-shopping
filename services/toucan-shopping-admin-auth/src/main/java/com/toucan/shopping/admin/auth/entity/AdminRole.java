package com.toucan.shopping.admin.auth.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户角色表
 */
@Data
public class AdminRole {
    private Integer id;


    /**
     * 所属用户
     */
    private String adminId;


    /**
     * 所属角色
     */
    private Long roleId;


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


}
