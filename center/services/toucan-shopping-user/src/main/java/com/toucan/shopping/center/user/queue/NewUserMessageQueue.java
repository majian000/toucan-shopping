package com.toucan.shopping.center.user.queue;

import com.toucan.shopping.center.user.kafka.message.UserCreateMessage;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 新用户队列
 */
@Component
public class NewUserMessageQueue {

    private ConcurrentLinkedQueue<UserCreateMessage> userCreateMessages = new ConcurrentLinkedQueue<UserCreateMessage>();

    public void push(UserCreateMessage userCreateMessage)
    {
        userCreateMessages.add(userCreateMessage);
    }

    public UserCreateMessage pop()
    {
        return userCreateMessages.poll();
    }
}
