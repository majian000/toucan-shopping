package com.toucan.shopping.admin.auth.export.vo;

import com.toucan.shopping.admin.auth.export.entity.AdminApp;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户
 */
@Data
public class AdminVO {

    private Integer id;


    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;


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

    /**
     * 管理员ID
     */
    private String adminId;

    /**
     * 所属应用列表
     */
    private List<AdminApp> adminApps;

    /**
     * 登录token
     */
    private String loginToken;


    /**
     * 前台传入
     */
    private String vcode;

}
