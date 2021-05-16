package com.toucan.shopping.standard.modules.apps.web.app.config;


import com.toucan.shopping.modules.common.lock.redis.RedisLock;
import com.toucan.shopping.modules.common.lock.redis.thread.RedisLockManagerThread;
import com.toucan.shopping.modules.common.properties.Toucan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 锁管理器线程启动
 */
@Configuration
public class LockManagerThreadConfig {

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private Toucan toucan;

    @Bean
    public RedisLockManagerThread redisLockManagerThread()
    {
        //设置全局锁表
        RedisLockManagerThread.globalLockTable = toucan.getAppCode() +"_"+RedisLockManagerThread.globalLockTable;

        RedisLockManagerThread lockManagerThread = new RedisLockManagerThread();
        lockManagerThread.setRedisLock(redisLock);
        lockManagerThread.setStringRedisTemplate(stringRedisTemplate);

        lockManagerThread.start();
        return lockManagerThread;
    }

}
