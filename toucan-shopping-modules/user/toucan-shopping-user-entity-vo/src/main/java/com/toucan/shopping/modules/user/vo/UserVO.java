package com.toucan.shopping.modules.user.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户
 */
@Data
public class UserVO {

    /**
     * ===================用户主表========================
     */

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * long类型的id转成字符串
     */
    private String userId;

    /**
     * 用户ID
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userMainId;

    /**
     * 用户ID字符串
     */
    private String userMainIdString;


    /**
     * 密码
     */
    private String password;

    /**
     * 所属应用
     */
    private String appCode ="10001001";

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;


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
     * 身份证
     */
    private String idCard;

    /**
     * 性别 0:女 1:男
     */
    private Short sex;

    /**
     * 用户类型 0:买家 1:卖家 2:既是买家又是卖家
     */
    private Short type;


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
