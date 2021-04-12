package com.toucan.shopping.cloud.user.app.config;


import com.toucan.shopping.cloud.user.kafka.thread.NewUserMessageQueueThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 将新创建的用户发送消息到kafka,以便同步到es
 */
@Component
@Order(value = 1)
public class KafkaNewUserMessageQueueThreadConfig implements ApplicationRunner {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NewUserMessageQueueThread newUserMessageQueueThread;



    @Override
    public void run(ApplicationArguments args)  {
        logger.info(" 开启NewUserMessageQueueThread线程...");
        newUserMessageQueueThread.start();
    }
}
