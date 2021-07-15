package com.toucan.shopping.modules.skylark.lock.service;

/**
 * 分布式锁
 */
public interface SkylarkLock {

    boolean lock(String lockKey,String lockValue);

    boolean lock(String lockKey,String lockValue,long millisecond);

    void unLock(String lockKey,String lockValue);
}
