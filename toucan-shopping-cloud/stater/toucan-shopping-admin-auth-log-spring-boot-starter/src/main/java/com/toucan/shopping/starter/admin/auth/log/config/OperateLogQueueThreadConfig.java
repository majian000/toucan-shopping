package com.toucan.shopping.starter.admin.auth.log.config;


import com.toucan.shopping.starter.admin.auth.log.thread.OperateLogQueueThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 操作日志线程
 */
@Component
@Order(value = 1)
public class OperateLogQueueThreadConfig implements ApplicationRunner {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OperateLogQueueThread operateLogQueueThread;



    @Override
    public void run(ApplicationArguments args)  {
        logger.info(" 开启OperateLogQueueThread线程.....");
        operateLogQueueThread.start();
    }
}
