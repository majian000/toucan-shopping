package com.toucan.shopping.modules.user.vo;

import com.toucan.shopping.modules.user.entity.UserApp;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserRegistVO {

    private Long id;

    /**
     * 用户主ID
     */
    private Long userMainId;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 用户名
     */
    private String username;

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
     * 用户所属应用
     */
    private List<UserApp> userApps;


    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

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
     * 身份证号
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


    //前台传入

    /**
     * 验证码
     */
    private String vcode;

}
