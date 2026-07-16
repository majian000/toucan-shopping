package com.toucan.shopping.modules.log.vo;

import lombok.Data;

import java.util.List;

/**
 * 日志参数请求对象
 */
@Data
public class LogParamRequest {

    private String uri; //请求地址

    private String ip; //请求IP

    private String contentType; //内容类型

    private String method; //请求方式

    private String body; //请求体

    private List<LogParamHeader> headers; //请求头


}
