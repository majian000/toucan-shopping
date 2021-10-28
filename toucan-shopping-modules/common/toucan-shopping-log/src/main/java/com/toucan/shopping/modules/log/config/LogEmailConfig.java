package com.toucan.shopping.modules.log.config;


import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.properties.modules.log.ReceiverProperty;
import com.toucan.shopping.modules.common.util.EmailUtils;
import com.toucan.shopping.modules.common.vo.email.EmailConfig;
import com.toucan.shopping.modules.common.vo.email.Receiver;
import com.toucan.shopping.modules.log.appender.LogEmailAppender;
import com.toucan.shopping.modules.log.queue.LogEmailQueue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class LogEmailConfig {


    @Autowired
    private Toucan toucan;

    @Autowired
    private LogEmailQueue logEmailQueue;


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
        if(CollectionUtils.isEmpty(toucan.getModules().getLog().getEmail().getReceiverList()))
        {
            throw new NullPointerException("日志邮件收件人列表为空");
        }

        //启动的时候并没有先加载配置中心的文件后再初始化对象,所以配置会有延迟,所以采用全局配置对象的这种方式处理
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
        //邮件标题
        LogEmailAppender.emailConfig.setSubject(toucan.getModules().getLog().getEmail().getTitle());
        //授权码
        LogEmailAppender.emailConfig.setSenderAccount(toucan.getModules().getLog().getEmail().getSenderAuthenticationCode());
        //收件人
        List<ReceiverProperty> receiverPropertyList = toucan.getModules().getLog().getEmail().getReceiverList();
        List<Receiver> receivers = new ArrayList<Receiver>();
        for(ReceiverProperty receiverProperty:receiverPropertyList)
        {
            if(StringUtils.isEmpty(receiverProperty.getEmail()))
            {
                throw new IllegalArgumentException("收件人邮箱不能为空");
            }
            if(!EmailUtils.isEmail(receiverProperty.getEmail()))
            {
                throw new IllegalArgumentException("收件人邮箱有误");
            }
            Receiver receiver = new Receiver();
            receiver.setEmail(receiverProperty.getEmail());
            receiver.setName(receiverProperty.getName());
            receivers.add(receiver);
        }
        LogEmailAppender.emailConfig.setReceivers(receivers);
        LogEmailAppender.logEmailQueue = logEmailQueue;
    }

}
