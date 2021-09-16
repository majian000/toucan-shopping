package com.toucan.shopping.modules.user.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户详情
 */
@Data
public class UserDetail {

    /**
     * 主键
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String trueName;

    /**
     * 头像
     */
    private String headSculpture;


    /**
     * 身份证
     */
    private String idCard;

    /**
     * 证件类型 1身份证 2护照 3海外
     */
    private Integer idcardType;

    /**
     * 身份证照片正面
     */
    private String idcardImg1;

    /**
     * 身份证照片背面
     */
    private String idcardImg2;

    /**
     * 实名状态 0未实名 1已实名
     */
    private Integer trueNameStatus = 0;

    /**
     * 用户ID,用该字段分库分表
     */
    private Long userMainId;

    /**
     * 性别 0:女 1:男
     */
    private Short sex;

    /**
     * 是否存在店铺 0不存在 1存在
     */
    private Short isShop;

    /**
     * 个性签名
     */
    private String personalizedSignature;

    /**
     * 是否会员 0否 1是
     */
    private Short isVip;
    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;


}
