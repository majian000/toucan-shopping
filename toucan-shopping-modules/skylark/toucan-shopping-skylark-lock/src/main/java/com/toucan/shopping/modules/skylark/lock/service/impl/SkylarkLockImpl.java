package com.toucan.shopping.modules.skylark.lock.service.impl;

import com.toucan.shopping.modules.common.lock.redis.RedisLock;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkylarkLockImpl implements SkylarkLock {

    @Autowired
    private RedisLock redisLock;

    @Override
    public boolean lock(String lockKey, String lockValue) {
        return redisLock.lock(lockKey, lockValue);
    }

    @Override
    public boolean lock(String lockKey, String lockValue, long millisecond) {
        return redisLock.lock(lockKey, lockValue, millisecond);
    }

    @Override
    public void unLock(String lockKey, String lockValue) {
        redisLock.unLock(lockKey, lockValue);
    }
}
