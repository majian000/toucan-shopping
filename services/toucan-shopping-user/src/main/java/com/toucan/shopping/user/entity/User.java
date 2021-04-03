package com.toucan.shopping.user.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户主表
 */
@Data
public class User {


    /**
     * 主键 雪花算法生成
     */
    private Long id;



    /**
     * 密码
     */
    private String password;



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
