package com.toucan.shopping.center.user.vo;

import com.toucan.shopping.center.user.entity.User;
import lombok.Data;

/**
 * 用户登录
 */
@Data
public class UserLoginVO {


    private Long id;

    /**
     * 手机号、邮箱、用户名
     */
    private String loginUserName;

    /**
     * 密码
     */
    private String password;



    /**
     * 登录token
     */
    private String loginToken;


    /**
     * 要登录的应用
     */
    private String appCode;


    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 用户名
     */
    private String username;

}
