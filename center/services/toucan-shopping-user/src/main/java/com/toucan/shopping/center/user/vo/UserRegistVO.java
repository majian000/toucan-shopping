package com.toucan.shopping.center.user.vo;

import com.toucan.shopping.center.user.entity.UserApp;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserRegistVO {

    private Long id;



    /**
     * 手机号
     */
    private String mobilePhone;

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


    //前台传入

    /**
     * 验证码
     */
    private String vcode;

}
