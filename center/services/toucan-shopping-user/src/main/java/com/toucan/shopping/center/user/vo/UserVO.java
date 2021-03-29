package com.toucan.shopping.center.user.vo;

import lombok.Data;

import java.util.Date;

/**
 * 用户
 */
@Data
public class UserVO {
    private Integer id;


    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String confirmPassword;

    /**
     * 启用状态 0:禁用 1启用
     */
    private Short enableStatus;


    /**
     * 创建时间
     */
    private Date createDate;


    /**
     * 所属应用
     */
    private String appCode ="10001001";

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    //前台传入

    /**
     * 验证码
     */
    private String vcode;


}
