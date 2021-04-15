package com.toucan.shopping.modules.user.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户登录记录
 */
@Data
public class UserLoginRecord {
    private Long id;

    /**
     * 用户ID
     */
    private String userId;


    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;



}
