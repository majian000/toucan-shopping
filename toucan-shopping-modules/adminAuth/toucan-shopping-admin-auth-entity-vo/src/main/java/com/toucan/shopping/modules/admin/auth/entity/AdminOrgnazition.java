package com.toucan.shopping.modules.admin.auth.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户组织机构表
 */
@Data
public class AdminOrgnazition {
    private Integer id;


    /**
     * 所属账号
     */
    private String adminId;


    /**
     * 所属组织机构
     */
    private String orgnazitionId;


    /**
     * 所属应用
     */
    private String appCode;

    /**
     * 创建时间
     */
    private Date createDate;



    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    /**
     * 创建人
     */
    private String createAdminId;


}
