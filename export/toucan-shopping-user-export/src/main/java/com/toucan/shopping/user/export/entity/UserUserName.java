package com.toucan.shopping.user.export.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户与用户名子表
 */
@Data
public class UserUserName {

    /**
     * 主键 分库用该
     */
    private Long id;


    /**
     * 用户名,分表用hash(username)
     */
    private String username;

    /**
     * 用户主表ID
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
