package com.toucan.shopping.admin.auth.web.vo.admin;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 管理员
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
     * 备注
     */
    private String adminId;

    /**
     * 验证码
     */
    private String vcode;

}
