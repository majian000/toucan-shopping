package com.toucan.shopping.center.admin.auth.entity;

import lombok.Data;

import java.util.Date;

/**
 * 部门
 */
@Data
public class Department {
    private Integer id;


    /**
     * 名称
     */
    private String name;


    /**
     * 上级菜单 -1表示当前是顶级
     */
    private Integer pid;

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
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    /**
     * 创建人
     */
    private String createAdminId;


}
