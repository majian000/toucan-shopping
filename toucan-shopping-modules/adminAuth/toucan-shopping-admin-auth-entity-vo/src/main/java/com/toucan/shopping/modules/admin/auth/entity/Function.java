package com.toucan.shopping.modules.admin.auth.entity;

import lombok.Data;

import java.util.Date;

/**
 * 功能项表
 */
@Data
public class Function {
    private Long id;


    /**
     * 名称
     */
    private String name;

    /**
     * 功能项ID
     */
    private String functionId;

    /**
     * 连接
     */
    private String link;

    /**
     * 权限标识
     */
    private String permission;


    /**
     * 菜单类型 0目录 1菜单 2按钮
     */
    private Short type;

    /**
     * 上级菜单 -1表示当前是顶级
     */
    private Long pid;

    /**
     * 启用状态 0:禁用 1启用
     */
    private Short enableStatus;

    /**
     * 图标
     */
    private String icon;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
    private Date updateDate;

    /**
     * 备注
     */
    private String remark;


    /**
     * 所属应用
     */
    private String appCode;

    /**
     * 排序
     */
    private Long functionSort;



    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    /**
     * 创建人
     */
    private String createAdminId;


    /**
     * 修改人
     */
    private String updateAdminId;


}
