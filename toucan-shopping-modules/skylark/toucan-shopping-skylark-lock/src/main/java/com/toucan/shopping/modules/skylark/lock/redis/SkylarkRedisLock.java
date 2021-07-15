package com.toucan.shopping.modules.skylark.lock.redis;

import org.springframework.data.redis.core.RedisTemplate;

public interface SkylarkRedisLock {

    /**
     * 默认key有效期毫秒
     */
    static long DEFAULT_MILLISECOND=5000;

    /**
     * 拿锁重试次数
     */
    static long DEFAULT_TRY_COUNT=1000;


    boolean lock(String lockKey, String lockValue);

    boolean lock(String lockKey, String lockValue, long millisecond);

    void unLock(String lockKey, String lockValue);

}
