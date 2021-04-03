package com.toucan.shopping.user.export.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户与邮箱子表
 */
@Data
public class UserEmail {


    /**
     * 主键,分库用
     */
    private Long id;

    /**
     * 邮箱,分表用
     */
    private String email;



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
