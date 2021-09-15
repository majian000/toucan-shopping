package com.toucan.shopping.cloud.user.app.config;


import com.toucan.shopping.cloud.user.thread.UserLoginHistoryQueueThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 保存用户登录历史记录
 */
@Component
@Order(value = 1)
public class UserLoginHistoryQueueThreadConfig implements ApplicationRunner {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserLoginHistoryQueueThread userLoginHistoryQueueThread;



    @Override
    public void run(ApplicationArguments args)  {
        logger.info(" 开启UserLoginHistoryQueueThread线程.....");
        userLoginHistoryQueueThread.start();
    }
}
