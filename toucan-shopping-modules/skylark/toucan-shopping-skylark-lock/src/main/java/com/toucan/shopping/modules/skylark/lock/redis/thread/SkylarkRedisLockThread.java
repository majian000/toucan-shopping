package com.toucan.shopping.modules.skylark.lock.redis.thread;

import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.skylark.lock.redis.SkylarkRedisLock;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 维持心跳线程(不断增加过期时间)
 */
@Data
public class SkylarkRedisLockThread extends Thread {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String lockKey;

    /**
     * 用于中途结束
     */
    private boolean loop=true;

    private RedisTemplate redisTemplate;

    public SkylarkRedisLockThread(String lockKey,RedisTemplate redisTemplate)
    {
        this.lockKey = lockKey;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run() {
        try {
            Date firstRunTime = DateUtils.currentDate();
            long millisecond = 0;
            if (redisTemplate != null) {
                //5分钟有效期
                while(loop&&millisecond<=(5*60)) {
                    redisTemplate.expire(lockKey, SkylarkRedisLock.DELAY_MILLISECOND, TimeUnit.SECONDS);
                    sleep(2000);
                    millisecond = DateUtils.subtract(firstRunTime,DateUtils.currentDate()) / 1000;
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }
}
