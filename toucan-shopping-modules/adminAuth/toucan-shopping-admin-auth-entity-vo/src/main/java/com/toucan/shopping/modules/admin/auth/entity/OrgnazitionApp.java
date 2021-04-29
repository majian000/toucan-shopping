package com.toucan.shopping.modules.admin.auth.entity;

import lombok.Data;

import java.util.Date;

/**
 * 组织机构应用表
 */
@Data
public class OrgnazitionApp {
    private Integer id;


    /**
     * 组织机构ID
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
