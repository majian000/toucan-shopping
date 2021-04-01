package com.toucan.shopping.center.user.export.vo;

import lombok.Data;

import java.util.Date;

/**
 * 用户
 */
@Data
public class UserVO {
    private Long id;


    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 邮箱
     */
    private String email;


    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String confirmPassword;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String trueName;

    /**
     * 头像
     */
    private String headSculpture;


    /**
     * 身份证
     */
    private String idCard;

    /**
     * 性别 0:女 1:男
     */
    private Short sex;

    /**
     * 用户类型 0:买家 1:卖家 2:既是买家又是卖家
     */
    private Short type;


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
