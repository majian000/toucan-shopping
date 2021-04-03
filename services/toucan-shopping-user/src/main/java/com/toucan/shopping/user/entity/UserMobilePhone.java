package com.toucan.shopping.user.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户与手机号子表
 */
@Data
public class UserMobilePhone {

    /**
     * 主键
     */
    private Long id;


    /**
     * 手机号
     */
    private String mobilePhone;


    /**
     * 用户主表ID,分库分表
     */
    private Long userMainId;


    /**
     * 创建时间
     */
    private Date createDate;


    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;


}
