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


    @SneakyThrows
    @Override
    protected void append(LoggingEvent loggingEvent) {
        if(enabled) {
            IThrowableProxy throwableProxy = loggingEvent.getThrowableProxy();
            if (throwableProxy != null&&emailConfig!=null) {
                Email email = new Email();

//
//                //设置收件人
//                List<Receiver> receivers = new ArrayList<Receiver>();
//                Receiver receiver = new Receiver();
//                receiver.setEmail("695391446@qq.com");
//                receiver.setName("user001");
//                receivers.add(receiver);
//                emailConfig.setReceivers(receivers);
//
//                email.setEmailConfig(emailConfig);
//
//                email.setSubject(DateUtils.format(DateUtils.currentDate(), DateUtils.FORMATTER_SS) + "——异常邮件");
//                email.setContent(LogbackThrowableProxyHelper.convertExceptionStack2StringByThrowable(throwableProxy));
//
//                EmailHelper.send(email);
            }
        }
    }
}
