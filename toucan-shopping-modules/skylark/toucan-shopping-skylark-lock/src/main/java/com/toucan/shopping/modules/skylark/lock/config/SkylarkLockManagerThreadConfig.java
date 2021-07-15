package com.toucan.shopping.modules.skylark.lock.config;


import com.toucan.shopping.modules.skylark.lock.redis.SkylarkRedisLock;
import com.toucan.shopping.modules.skylark.lock.redis.impl.SkylarkRedisLockImpl;
import com.toucan.shopping.modules.skylark.lock.redis.thread.SkylarkRedisLockManagerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 锁管理器线程启动
 */
@Configuration
public class SkylarkLockManagerThreadConfig {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private SkylarkRedisLock redisLock;


    @Bean
    public SkylarkRedisLockManagerThread redisLockManagerThread()
    {
        logger.info("初始化 SkylarkRedisLockManagerThread(云雀锁管理器).......");

        //设置全局锁表
        SkylarkRedisLockManagerThread.globalLockTable = "toucan_shopping_"+SkylarkRedisLockManagerThread.globalLockTable;

        SkylarkRedisLockManagerThread lockManagerThread = new SkylarkRedisLockManagerThread();
        lockManagerThread.setRedisLock(redisLock);
        lockManagerThread.setRedisTemplate(((SkylarkRedisLockImpl)redisLock).getRedisTemplate());

        lockManagerThread.start();
        return lockManagerThread;
    }

}
