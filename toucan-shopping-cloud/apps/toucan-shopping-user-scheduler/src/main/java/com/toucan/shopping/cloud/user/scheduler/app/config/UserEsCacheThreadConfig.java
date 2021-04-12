package com.toucan.shopping.cloud.user.scheduler.app.config;


import com.toucan.shopping.user.export.page.UserPageInfo;
import com.toucan.shopping.cloud.user.scheduler.thread.UserEsCacheThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 用户初始化到ES
 */
@Component
@Order(value = 1)
public class UserEsCacheThreadConfig implements ApplicationRunner {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserEsCacheThread userEsCacheThread;



    @Override
    public void run(ApplicationArguments args)  {
        userEsCacheThread.start();
    }
}
