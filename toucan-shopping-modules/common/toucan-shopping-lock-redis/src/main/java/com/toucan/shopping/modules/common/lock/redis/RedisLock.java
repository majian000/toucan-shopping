package com.toucan.shopping.modules.common.lock.redis;

public interface RedisLock {

    /**
     * 默认key有效期毫秒
     */
    long DEFAULT_MILLISECOND = 30000;



    boolean lock(String lockKey, String lockValue);

    boolean lock(String lockKey, String lockValue, long millisecond);

    void unLock(String lockKey, String lockValue);
}
