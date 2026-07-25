package com.toucan.shopping.modules.common.lock.redis.impl;

import com.toucan.shopping.modules.common.lock.redis.thread.RedisLockManagerThread;
import com.toucan.shopping.modules.common.lock.redis.RedisLock;
import com.toucan.shopping.modules.common.util.RenewKeysBucket;
import jakarta.annotation.Resource;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Data
public class RedisLockImpl implements RedisLock {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 续期key分片桶,key通过hash路由到不同分片,每个续期线程绑定一个分片
     */
    @Resource(name = "renewKeysBucket")
    private RenewKeysBucket renewKeysBucket;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 最大重试次数,可通过setter覆盖(默认1000)
     */
    private long maxRetryCount = RedisLock.DEFAULT_TRY_COUNT;

    /**
     * 重试间隔(毫秒),可通过setter覆盖(默认50)
     */
    private long retrySleepMs = 50;


    public boolean lock(String lockKey, String lockValue) {
        return lock(lockKey, lockValue, RedisLock.DEFAULT_MILLISECOND);
    }


    public boolean lock(String lockKey, String lockValue, long millisecond) {

        int tryCount = 1;
        while (true) {
            if (tryCount >= maxRetryCount) {
                logger.warn("redis key " + lockKey + " 已存在 重试次数已到" + maxRetryCount);
                break;
            }
            tryCount++;
            //利用setIfAbsent原子操作同时设置key和过期时间,避免SETNX+EXPIRE两步操作的非原子性问题
            Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, millisecond, TimeUnit.MILLISECONDS);
            if (result != null && result.booleanValue()) {
                //将key加入续期分片桶,由对应分片的续期线程负责续期
                renewKeysBucket.put(lockKey, millisecond);

                //将key保存到锁表中
                stringRedisTemplate.opsForHash().put(RedisLockManagerThread.globalLockTable, lockKey, String.valueOf(System.currentTimeMillis()));

                return true;
            }
            //重试间隔,避免忙等对Redis造成压力
            try {
                Thread.sleep(retrySleepMs);
            } catch (InterruptedException e) {
                logger.warn("lock retry sleep interrupted for key " + lockKey, e);
                Thread.currentThread().interrupt();
                break;
            }
        }
        return false;
    }


    public void unLock(String lockKey, String lockValue) {
        String redisLockValue = stringRedisTemplate.opsForValue().get(lockKey);
        //防止别人误操作释放锁 判断传进来的值与缓存存储的值是否一致
        if (redisLockValue == null || lockValue.equals(redisLockValue)) {
            //从续期分片桶中移除
            renewKeysBucket.remove(lockKey);

            stringRedisTemplate.opsForValue().getOperations().delete(lockKey);

            //从锁表中删除这个锁
            stringRedisTemplate.opsForHash().delete(RedisLockManagerThread.globalLockTable, lockKey);
        }
    }


}
