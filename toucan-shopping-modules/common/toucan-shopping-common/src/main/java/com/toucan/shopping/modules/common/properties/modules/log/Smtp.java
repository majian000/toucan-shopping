package com.toucan.shopping.modules.common.properties.modules.log;

import lombok.Data;

/**
 * 自定义日志邮件
 */
@Data
public class Smtp {

    /**
     * SMTP服务器
     */
    private String host;

    /**
     * SMTP认证
     */
    private String auth;

    /**
     * 端口
     */
    private String port;

    /**
     * 默认套接字实现类
     */
    private String socketFactoryClass;

    /**
     * 套接字是否需要回调
     */
    private String socketFactoryFallback;

}
