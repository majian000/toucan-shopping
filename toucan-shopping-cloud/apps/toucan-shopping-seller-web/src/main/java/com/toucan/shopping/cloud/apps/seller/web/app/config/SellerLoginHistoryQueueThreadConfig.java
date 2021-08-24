package com.toucan.shopping.cloud.apps.seller.web.app.config;


import com.toucan.shopping.cloud.apps.seller.web.queue.SellerLoginHistoryQueue;
import com.toucan.shopping.cloud.apps.seller.web.thread.SellerLoginHistoryQueueThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 保存登录卖家的历史记录
 */
@Component
@Order(value = 1)
public class SellerLoginHistoryQueueThreadConfig implements ApplicationRunner {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SellerLoginHistoryQueueThread sellerLoginHistoryQueueThread;



    @Override
    public void run(ApplicationArguments args)  {
        logger.info(" 开启SellerLoginHistoryQueueThread线程.....");
        sellerLoginHistoryQueueThread.start();
    }
}
