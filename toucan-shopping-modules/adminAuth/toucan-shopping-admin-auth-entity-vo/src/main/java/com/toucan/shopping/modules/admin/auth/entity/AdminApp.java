package com.toucan.shopping.modules.admin.auth.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户应用关联表
 */
@Data
public class AdminApp {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * 管理员ID
     */
    private String adminId;

    /**
     * 所属应用
     */
    private String appCode;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;


    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    /**
     * 创建人
     */
    private String createAdminId;


    /**
     * 登录时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date loginDate;


    /**
     * 保存状态,在批量保存的时候返回哪些保存失败了
     */
    public int success=0;

    /**
     * 状态消息 返回为什么失败
     */
    private String msg;


    /**
     * 登录状态(0未登录 1登陆中)
     */
    private Short loginStatus;



}
