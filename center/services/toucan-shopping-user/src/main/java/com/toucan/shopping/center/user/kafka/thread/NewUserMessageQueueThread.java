package com.toucan.shopping.center.user.kafka.thread;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.center.user.kafka.constant.UserMessageTopicConstant;
import com.toucan.shopping.center.user.kafka.message.UserCreateMessage;
import com.toucan.shopping.center.user.queue.NewUserMessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class NewUserMessageQueueThread extends Thread {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NewUserMessageQueue newUserMessageQueue;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public void run() {

        while(true)
        {
            try {
                UserCreateMessage userCreateMessage = newUserMessageQueue.pop();
                if (userCreateMessage != null) {
                    kafkaTemplate.send(UserMessageTopicConstant.user_create.name(), JSONObject.toJSONString(userCreateMessage));
                } else {
                    //休眠5秒钟
                    this.sleep(5000);
                }
            }catch(Exception e)
            {
                logger.warn(e.getMessage(),e);
            }
        }
    }
}
