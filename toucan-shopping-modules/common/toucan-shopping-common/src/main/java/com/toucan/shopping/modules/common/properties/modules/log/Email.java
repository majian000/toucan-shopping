package com.toucan.shopping.modules.common.properties.modules.log;

import lombok.Data;

/**
 * 自定义日志邮件
 */
@Data
public class Email {

    /**
     * SMTP服务器配置
     */
    private Smtp smtp;

    /**
     * 协议
     */
    private String protocol="smtp";


    /**
     * 是否开启
     */
    private boolean enabled;

    /**
     * 发件人
     */
    private String sender;


    /**
     * 发件人授权码
     */
    private String senderAuthenticationCode;

    /**
     * 收件人列表多个用,分割
     */
    private String receiverList;


}
