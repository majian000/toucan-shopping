package com.toucan.shopping.modules.log.config;


import com.toucan.shopping.modules.log.thread.LogParamQueueThread;
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
public class LogParamQueueThreadConfig implements ApplicationRunner {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private LogParamQueueThread logParamQueueThread;



    @Override
    public void run(ApplicationArguments args)  {
        logger.info("开启LogParamQueueThread线程.....");
        logParamQueueThread.start();
    }
}
