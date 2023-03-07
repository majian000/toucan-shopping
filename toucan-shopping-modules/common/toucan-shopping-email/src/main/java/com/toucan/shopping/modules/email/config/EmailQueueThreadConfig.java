package com.toucan.shopping.modules.email.config;


import com.toucan.shopping.modules.email.thread.EmailQueueThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 日志邮件线程
 */
@Component
@Order(value = 1)
public class EmailQueueThreadConfig implements ApplicationRunner {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EmailQueueThread emailQueueThread;



    @Override
    public void run(ApplicationArguments args)  {
        logger.info("开启EmailQueueThread线程.....");
        emailQueueThread.start();
    }
}
