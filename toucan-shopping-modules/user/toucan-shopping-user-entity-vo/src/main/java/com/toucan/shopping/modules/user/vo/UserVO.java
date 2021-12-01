package com.toucan.shopping.modules.user.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.user.entity.User;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户
 */
@Data
public class UserVO extends User {

    /**
     * ===================用户主表========================
     */

    /**
     * long类型的id转成字符串
     */
    private String userId;

    /**
     * 用户ID字符串
     */
    private String userMainIdString;



    /**
     * 所属应用
     */
    private String appCode ="10001001";




    //=====================用户手机号关联表====================

    /**
     * 手机号
     */
    private String mobilePhone;


    //===============用户邮箱关联=========================

    /**
     * 邮箱
     */
    private String email;


    //===============用户和用户名关联======================

    /**
     * 用户名
     */
    private String username;


    /**
     * 确认密码
     */
    private String confirmPassword;

    //==============用户和用户详情关联======================

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
     * 头像
     */
    private String httpHeadSculpture;

    /**
     * 身份证照片正面
     */
    private String idcardImg1;
    /**
     * 身份证照片正面
     */
    private String httpIdcardImg1;


    /**
     * 身份证照片背面
     */
    private String idcardImg2;

    /**
     * 身份证照片背面
     */
    private String httpIdcardImg2;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 实名状态 0未实名 1已实名
     */
    private Integer trueNameStatus = 0;

    /**
     * 证件类型 1身份证 2护照 3海外
     */
    private Integer idcardType;

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
     * 启用状态 0:禁用 1启用
     */
    private Short enableStatus;





    //=================前台传入====================

    /**
     * 验证码
     */
    private String vcode;


}
