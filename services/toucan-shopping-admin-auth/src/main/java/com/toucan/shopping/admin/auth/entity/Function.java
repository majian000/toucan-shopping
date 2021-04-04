package com.toucan.shopping.admin.auth.entity;

import lombok.Data;

import java.util.Date;

/**
 * 功能项表
 */
@Data
public class Function {
    private Integer id;


    /**
     * 名称
     */
    private String name;

    /**
     * 连接
     */
    private String link;


    /**
     * 菜单类型 0目录 1菜单 2按钮
     */
    private Short type;

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
