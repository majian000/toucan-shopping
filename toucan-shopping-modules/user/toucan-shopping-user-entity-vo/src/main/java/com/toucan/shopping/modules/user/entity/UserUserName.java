package com.toucan.shopping.modules.user.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户与用户名子表
 */
@Data
public class UserUserName {

    /**
     * 主键
     */
    private Long id;


    /**
     * 用户名
     */
    private String username;

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
