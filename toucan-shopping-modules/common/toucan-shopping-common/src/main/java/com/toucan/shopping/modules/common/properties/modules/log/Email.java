package com.toucan.shopping.modules.common.properties.modules.log;

import lombok.Data;

import java.util.List;

/**
 * 自定义日志邮件
 */
@Data
public class Email {

    /**
     * 邮件标题
     */
    private String title;

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
     * 收件人列表
     */
    private List<ReceiverProperty> receiverList;


}
