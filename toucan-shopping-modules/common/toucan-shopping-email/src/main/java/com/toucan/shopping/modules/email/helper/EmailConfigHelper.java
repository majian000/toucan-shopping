package com.toucan.shopping.modules.email.helper;

import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.email.EmailConfig;

public class EmailConfigHelper {
    public static Toucan toucan;

    public static EmailConfig getEmailConfig(String subject)
    {
        if(!toucan.getModules().getEmail().isEnabled())
        {
            return null;
        }
        EmailConfig emailConfig = new EmailConfig();
        //SMTP服务器配置
        emailConfig.setSmtpServer(toucan.getModules().getEmail().getSmtp().getHost());
        emailConfig.setProtocol(toucan.getModules().getEmail().getProtocol());
        emailConfig.setSmtpAuth(toucan.getModules().getEmail().getSmtp().getAuth());
        emailConfig.setSmtpSocketFactoryClass(toucan.getModules().getEmail().getSmtp().getSocketFactoryClass());
        emailConfig.setSmtpSocketFactoryFallback(toucan.getModules().getEmail().getSmtp().getSocketFactoryFallback());

        //发件人
        emailConfig.setSender(toucan.getModules().getEmail().getSender());
        //邮件标题
        emailConfig.setSubject(subject);
        //授权码
        emailConfig.setSenderAccount(toucan.getModules().getEmail().getSenderAuthenticationCode());
        return emailConfig;
    }
}
