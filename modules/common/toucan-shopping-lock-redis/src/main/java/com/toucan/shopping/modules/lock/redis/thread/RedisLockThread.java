package com.toucan.shopping.modules.lock.redis.thread;

import com.toucan.shopping.modules.lock.redis.RedisLock;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 维持心跳线程(不断增加过期时间)
 */
@Data
public class RedisLockThread extends Thread {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String lockKey;

    private boolean loop=true;

    private RedisTemplate redisTemplate;


    @Override
    public void run() {
        try {
            if (redisTemplate != null) {
                while(loop) {
                    redisTemplate.expire(lockKey, RedisLock.DEFAULT_MILLISECOND, TimeUnit.SECONDS);
                    sleep(2000);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }
}
