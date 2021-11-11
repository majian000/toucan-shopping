package com.toucan.shopping.modules.log.appender;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import com.toucan.shopping.modules.common.properties.modules.log.Log;
import com.toucan.shopping.modules.common.properties.modules.log.ReceiverProperty;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.EmailHelper;
import com.toucan.shopping.modules.common.util.EmailUtils;
import com.toucan.shopping.modules.common.util.ExceptionHelper;
import com.toucan.shopping.modules.common.vo.email.Email;
import com.toucan.shopping.modules.common.vo.email.EmailConfig;
import com.toucan.shopping.modules.common.vo.email.Receiver;
import com.toucan.shopping.modules.log.helper.LogbackThrowableProxyHelper;
import com.toucan.shopping.modules.log.message.LogEmailMessage;
import com.toucan.shopping.modules.log.queue.LogEmailQueue;
import com.toucan.shopping.modules.log.validate.LogConfigValidator;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class LogEmailAppender extends AppenderBase<LoggingEvent> {

    public static EmailConfig emailConfig = null;

    /**
     * 是否开启邮件
     */
    public static boolean enabled;

    public static LogEmailQueue logEmailQueue;

    /**
     * 刷新配置
     */
    public static void flushConfig(Log log){

        LogConfigValidator.validate(log);

        LogEmailAppender.emailConfig = new EmailConfig();
        LogEmailAppender.enabled = log.getEmail().isEnabled();
        //忽略异常列表
        LogEmailAppender.emailConfig.setIgnoreExceptionList(log.getEmail().getIgnoreExceptionList());

        //SMTP服务器配置
        LogEmailAppender.emailConfig.setSmtpServer(log.getEmail().getSmtp().getHost());
        LogEmailAppender.emailConfig.setProtocol(log.getEmail().getProtocol());
        LogEmailAppender.emailConfig.setSmtpAuth(log.getEmail().getSmtp().getAuth());
        LogEmailAppender.emailConfig.setSmtpSocketFactoryClass(log.getEmail().getSmtp().getSocketFactoryClass());
        LogEmailAppender.emailConfig.setSmtpSocketFactoryFallback(log.getEmail().getSmtp().getSocketFactoryFallback());

        //发件人
        LogEmailAppender.emailConfig.setSender(log.getEmail().getSender());
        //邮件标题
        LogEmailAppender.emailConfig.setSubject(log.getEmail().getTitle());
        //授权码
        LogEmailAppender.emailConfig.setSenderAccount(log.getEmail().getSenderAuthenticationCode());
        //收件人
        List<ReceiverProperty> receiverPropertyList = log.getEmail().getReceiverList();
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

    }


    @SneakyThrows
    @Override
    protected void append(LoggingEvent loggingEvent) {
        if(enabled) {
            IThrowableProxy throwableProxy = loggingEvent.getThrowableProxy();
            if (throwableProxy != null&&emailConfig!=null&&logEmailQueue!=null) {
                //判断是否该忽略这个异常
                if(CollectionUtils.isNotEmpty(emailConfig.getIgnoreExceptionList()))
                {
                    for(String ignoreException:emailConfig.getIgnoreExceptionList())
                    {
                        //忽略掉这个异常
                        if(throwableProxy.getClassName().equals(ignoreException))
                        {
                            return;
                        }
                    }
                }
                Email email = new Email();
                email.setEmailConfig(emailConfig);
                email.setSubject(DateUtils.format(DateUtils.currentDate(), DateUtils.FORMATTER_SS) + emailConfig.getSubject());
                email.setContent(LogbackThrowableProxyHelper.convertExceptionStack2StringByThrowable(throwableProxy));

                LogEmailMessage logEmailMessage = new LogEmailMessage();
                logEmailMessage.setEmail(email);
                logEmailQueue.push(logEmailMessage);

            }
        }
    }
}
