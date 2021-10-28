package com.toucan.shopping.modules.log.config;


import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.email.EmailConfig;
import com.toucan.shopping.modules.log.appender.LogEmailAppender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class LogEmailConfig {


    @Autowired
    private Toucan toucan;



    @PostConstruct
    public void initConfig()
    {
        if(toucan.getModules().getLog()==null)
        {
            throw new NullPointerException("日志没有配置参数");
        }
        if(toucan.getModules().getLog().getEmail()==null)
        {
            throw new NullPointerException("日志邮件没有配置参数");
        }
        if(toucan.getModules().getLog().getEmail().getSmtp()==null)
        {
            throw new NullPointerException("日志邮件SMTP配置没有找到");
        }

        LogEmailAppender.emailConfig = new EmailConfig();
        LogEmailAppender.enabled = toucan.getModules().getLog().getEmail().isEnabled();
        //SMTP服务器配置
        LogEmailAppender.emailConfig.setSmtpServer(toucan.getModules().getLog().getEmail().getSmtp().getHost());
        LogEmailAppender.emailConfig.setProtocol(toucan.getModules().getLog().getEmail().getProtocol());
        LogEmailAppender.emailConfig.setSmtpAuth(toucan.getModules().getLog().getEmail().getSmtp().getAuth());
        LogEmailAppender.emailConfig.setSmtpSocketFactoryClass(toucan.getModules().getLog().getEmail().getSmtp().getSocketFactoryClass());
        LogEmailAppender.emailConfig.setSmtpSocketFactoryFallback(toucan.getModules().getLog().getEmail().getSmtp().getSocketFactoryFallback());
        //发件人
        LogEmailAppender.emailConfig.setSender(toucan.getModules().getLog().getEmail().getSender());
        //授权码
        LogEmailAppender.emailConfig.setSenderAccount(toucan.getModules().getLog().getEmail().getSenderAuthenticationCode());
        System.out.println(toucan);
    }

}
