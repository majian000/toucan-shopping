package com.toucan.shopping.modules.admin.auth.log.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 请求日志
 */
@Data
public class OperateLog {


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求地址
     */
    private String uri;

    /**
     * 参数
     */
    private String params;

    /**
     * 功能名称
     */
    private String functionName;

    /**
     * 访问IP
     */
    private String ip;

    /**
     * 功能ID
     */
    private String functionId;

    /**
     * 备注
     */
    private String remark;

    /**
     *应用编码
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



}
