package com.toucan.shopping.cloud.seller.app.config;


import com.toucan.shopping.modules.common.lock.redis.RedisLock;
import com.toucan.shopping.modules.common.lock.redis.thread.RedisLockManagerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 锁管理器线程启动
 */
@Configuration
public class LockManagerThreadConfig {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private RedisLock redisLock;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Bean
    public RedisLockManagerThread redisLockManagerThread()
    {
        logger.info("初始化应用编码 RedisLockManagerThread(锁管理器).......");

        //设置全局锁表
        RedisLockManagerThread.globalLockTable = "toucan_shopping_"+RedisLockManagerThread.globalLockTable;

        RedisLockManagerThread lockManagerThread = new RedisLockManagerThread();
        lockManagerThread.setRedisLock(redisLock);
        lockManagerThread.setStringRedisTemplate(stringRedisTemplate);

        lockManagerThread.start();
        return lockManagerThread;
    }

}
