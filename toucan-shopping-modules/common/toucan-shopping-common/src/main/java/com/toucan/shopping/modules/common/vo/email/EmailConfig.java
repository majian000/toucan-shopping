package com.toucan.shopping.modules.common.vo.email;


import lombok.Data;

import java.util.List;
import java.util.Properties;

/**
 * 邮件配置对象
 */
@Data
public class EmailConfig {

    /**
     * 协议
     */
    private String protocol="smtp";

    /**
     * smtp服务器的地址
     */
    private String smtpServer;

    /**
     * smtp是否需要认证
     */
    private String smtpAuth = "true";

    /**
     * 默认套接字实现类
     */
    private String smtpSocketFactoryClass = "javax.net.ssl.SSLSocketFactory";

    /**
     * 套接字是否需要回调
     */
    private String smtpSocketFactoryFallback="false";


    /**
     * 邮件标题
     */
    private String subject;

    /**
     * 发送人
     */
    private String sender;

    /**
     * 发送人授权码
     */
    private String senderAccount;

    /**
     * 发送人名称
     */
    private String senderName;


    /**
     * 收件人
     */
    private List<Receiver> receivers;

    /**
     * 是否需要请求认证
     */
    private boolean isAuth=true;


    /**
     *SMTP服务器端口,默认为25,如果开启了SSL,需要自行查看端口,例如163邮箱为465
     */
    private String port="465";


    /**
     * 返回配置对象
     * @return
     */
    public Properties getProperties()
    {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", protocol);
        props.setProperty("mail.smtp.host", smtpServer);
        props.setProperty("mail.smtp.auth", smtpAuth);
        props.setProperty("mail.smtp.port", port);
        props.setProperty("mail.smtp.socketFactory.class", smtpSocketFactoryClass);
        props.setProperty("mail.smtp.socketFactory.fallback", smtpSocketFactoryFallback);
        props.setProperty("mail.smtp.socketFactory.port", port);

        return props;
    }


}
