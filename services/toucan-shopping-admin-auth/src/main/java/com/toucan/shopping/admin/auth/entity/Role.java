package com.toucan.shopping.admin.auth.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 角色表
 */
@Data
public class Role {
    private Integer id;


    /**
     * 角色名
     */
    private String name;



    /**
     * 启用状态 0:禁用 1启用
     */
    private Short enableStatus;


    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 备注
     */
    private String remark;


    /**
     * 所属应用
     */
    private String appCode;


    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;
    /**
     * 创建人
     */
    private String createAdminId;


}
