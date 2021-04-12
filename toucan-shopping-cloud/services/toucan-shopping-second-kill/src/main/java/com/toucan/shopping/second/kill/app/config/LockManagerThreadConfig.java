package com.toucan.shopping.second.kill.app.config;


import com.toucan.shopping.lock.redis.RedisLock;
import com.toucan.shopping.lock.redis.thread.RedisLockManagerThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${toucan.app-code}")
    private String appCode;

    @Bean
    public RedisLockManagerThread redisLockManagerThread()
    {
        //设置全局锁表
        RedisLockManagerThread.globalLockTable = appCode +"_"+RedisLockManagerThread.globalLockTable;

        RedisLockManagerThread lockManagerThread = new RedisLockManagerThread();
        lockManagerThread.setRedisLock(redisLock);
        lockManagerThread.setStringRedisTemplate(stringRedisTemplate);

        lockManagerThread.start();
        return lockManagerThread;
    }

}
