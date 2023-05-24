package com.toucan.shopping.modules.email.queue;

import com.toucan.shopping.modules.email.message.EmailMessage;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 日志邮件队列
 */
@Component
public class EmailQueue {


    private ConcurrentLinkedQueue<EmailMessage> logEmailMessages = new ConcurrentLinkedQueue<EmailMessage>();

    public void push(EmailMessage userCreateMessage)
    {
        logEmailMessages.add(userCreateMessage);
    }

    public EmailMessage pop()
    {
        return logEmailMessages.poll();
    }

}
