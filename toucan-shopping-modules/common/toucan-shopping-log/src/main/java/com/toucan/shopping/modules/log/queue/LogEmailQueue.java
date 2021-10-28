package com.toucan.shopping.modules.log.queue;

import com.toucan.shopping.modules.log.message.LogEmailMessage;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 日志邮件队列
 */
@Component
public class LogEmailQueue {


    private ConcurrentLinkedQueue<LogEmailMessage> logEmailMessages = new ConcurrentLinkedQueue<LogEmailMessage>();

    public void push(LogEmailMessage userCreateMessage)
    {
        logEmailMessages.add(userCreateMessage);
    }

    public LogEmailMessage pop()
    {
        return logEmailMessages.poll();
    }

}
