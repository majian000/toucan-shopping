package com.toucan.shopping.modules.log.vo;

import lombok.Data;

/**
 * 日志参数响应对象
 */
@Data
public class LogParamResponse {

    private String uri; //请求地址

    private String contentType; //内容类型

    private String body; //响应体


}
