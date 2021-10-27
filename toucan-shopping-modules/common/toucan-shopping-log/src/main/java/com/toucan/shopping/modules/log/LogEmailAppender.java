package com.toucan.shopping.modules.log;

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
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class LogEmailAppender extends AppenderBase<LoggingEvent> {


    @SneakyThrows
    @Override
    protected void append(LoggingEvent loggingEvent) {

        Email email = new Email();
        EmailConfig emailConfig = new EmailConfig();
        emailConfig.setSmtpServer("smtp.163.com");
        emailConfig.setSender("mmdrss@163.com");
        emailConfig.setSenderAccount("IJHWJFZINFUPQIUC");

        //设置收件人
        List<Receiver> receivers = new ArrayList<Receiver>();
        Receiver receiver = new Receiver();
        receiver.setEmail("695391446@qq.com");
        receiver.setName("user001");
        receivers.add(receiver);
        emailConfig.setReceivers(receivers);

        email.setEmailConfig(emailConfig);

        email.setSubject(DateUtils.format(DateUtils.currentDate(),DateUtils.FORMATTER_SS)+"——异常邮件");
        IThrowableProxy throwableProxy = loggingEvent.getThrowableProxy();
        //email.setContent(ExceptionHelper.convertExceptionStack2StringByThrowable(loggingEvent.getThrowable()))


//        EmailHelper.send(email);

    }
}
