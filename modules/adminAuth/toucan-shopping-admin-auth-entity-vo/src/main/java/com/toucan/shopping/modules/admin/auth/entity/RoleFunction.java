package com.toucan.shopping.modules.admin.auth.entity;

import lombok.Data;

import java.util.Date;

/**
 * 角色功能项表
 */
@Data
public class RoleFunction {
    private Integer id;


    /**
     * 所属角色
     */
    private String roleId;

    /**
     * 所属菜单
     */
    private String functionId;


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
