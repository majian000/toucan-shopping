package com.toucan.shopping.modules.skylark.lock.service.impl;

import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.skylark.lock.redis.SkylarkRedisLock;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkylarkLockImpl implements SkylarkLock {

    @Autowired
    private SkylarkRedisLock redisLock;

    @Autowired
    private Toucan toucan;

    @Override
    public boolean lock(String lockKey, String lockValue) {
        if("redis".equals(toucan.getModules().getSkylarkLock().getType())) {
            return redisLock.lock(lockKey, lockValue);
        }
        return false;
    }

    @Override
    public boolean lock(String lockKey, String lockValue, long millisecond) {
        if("redis".equals(toucan.getModules().getSkylarkLock().getType())) {
            return redisLock.lock(lockKey, lockValue,millisecond);
        }
        return false;
    }

    @Override
    public void unLock(String lockKey, String lockValue) {
        if("redis".equals(toucan.getModules().getSkylarkLock().getType())) {
            redisLock.unLock(lockKey, lockValue);
        }
    }
}
