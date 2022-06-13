package com.toucan.shopping.modules.admin.auth.log.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 请求日志
 */
@Data
public class RequestLog {
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
     * 内容
     */
    private String body;

    /**
     * 功能名称
     */
    private String functionName;

    /**
     * 访问IP
     */
    private String ip;


    /**
     * 备注
     */
    private String remark;

    /**
     *编码
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
