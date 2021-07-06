package com.toucan.shopping.cloud.apps.scheduler.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 商城用户
 */
@Data
public class UserVo {
    private Integer id;


    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

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
     * 所属应用
     */
    private String appCode ="10001001";

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
