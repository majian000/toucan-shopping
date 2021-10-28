package com.toucan.shopping.modules.log.appender;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.EmailHelper;
import com.toucan.shopping.modules.common.util.ExceptionHelper;
import com.toucan.shopping.modules.common.vo.email.Email;
import com.toucan.shopping.modules.common.vo.email.EmailConfig;
import com.toucan.shopping.modules.common.vo.email.Receiver;
import com.toucan.shopping.modules.log.helper.LogbackThrowableProxyHelper;
import com.toucan.shopping.modules.log.message.LogEmailMessage;
import com.toucan.shopping.modules.log.queue.LogEmailQueue;
import lombok.Data;
import lombok.SneakyThrows;

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


    @SneakyThrows
    @Override
    protected void append(LoggingEvent loggingEvent) {
        if(enabled) {
            IThrowableProxy throwableProxy = loggingEvent.getThrowableProxy();
            if (throwableProxy != null&&emailConfig!=null&&logEmailQueue!=null) {
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
