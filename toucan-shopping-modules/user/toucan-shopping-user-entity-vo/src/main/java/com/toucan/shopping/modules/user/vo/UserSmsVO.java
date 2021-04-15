package com.toucan.shopping.modules.user.vo;

import lombok.Data;

import java.util.Date;

/**
 * 用户短信
 */
@Data
public class UserSmsVO {



    /**
     * 手机号
     */
    private String mobilePhone;


    /**
     * 1:注册 2:登录
     */
    private Integer type;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 消息内容
     */
    private String msg;




}
