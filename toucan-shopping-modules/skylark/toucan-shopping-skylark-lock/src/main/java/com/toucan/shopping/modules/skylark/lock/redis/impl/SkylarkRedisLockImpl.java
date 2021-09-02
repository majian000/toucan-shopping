package com.toucan.shopping.modules.skylark.lock.redis.impl;

import com.toucan.shopping.modules.skylark.lock.redis.SkylarkRedisLock;
import com.toucan.shopping.modules.skylark.lock.redis.thread.SkylarkRedisLockManagerThread;
import com.toucan.shopping.modules.skylark.lock.redis.thread.SkylarkRedisLockThread;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@Data
public class SkylarkRedisLockImpl implements SkylarkRedisLock {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, SkylarkRedisLockThread> threadHashMap=new ConcurrentHashMap<String,SkylarkRedisLockThread>();


    @Autowired
    @Qualifier("skylarkLockRedisTemplate")
    private RedisTemplate redisTemplate;



    public boolean lock(String lockKey,String lockValue)
    {
        return lock(lockKey,lockValue, SkylarkRedisLock.DEFAULT_MILLISECOND);
    }


    public boolean lock(String lockKey,String lockValue,long millisecond)
    {

        int tryCount=1;
        while(true) {
            if(tryCount>=DEFAULT_TRY_COUNT)
            {
                logger.warn("redis key "+lockKey+" 已存在 重试次数已到"+DEFAULT_TRY_COUNT);
                break;
            }
            tryCount++;
            //利用setnx 设置一个key
            Boolean result= redisTemplate.opsForValue().setIfAbsent(lockKey,lockValue);
            if (result!=null&&result.booleanValue()) {
                //维持key有效期
                redisTemplate.expire(lockKey,millisecond,TimeUnit.MILLISECONDS);
                if(threadHashMap.get(lockKey+"_thread")!=null)
                {
                    threadHashMap.get(lockKey+"_thread").setLoop(false);
                }
                //维持心跳
                SkylarkRedisLockThread expireThread = new SkylarkRedisLockThread(lockKey,redisTemplate);
                threadHashMap.put(lockKey+"_thread",expireThread);
                expireThread.start();

                //将key保存到锁表中
                redisTemplate.opsForHash().put(SkylarkRedisLockManagerThread.globalLockTable,lockKey,String.valueOf(System.currentTimeMillis()));

                return true;
            }
        }
        return false;
    }



    public void unLock(String lockKey,String lockValue)
    {
        //防止别人误操作释放锁 判断传进来的值与缓存存储的值是否一致
        if(lockValue.equals(redisTemplate.opsForValue().get(lockKey)))
        {
            if(threadHashMap.get(lockKey+"_thread")!=null)
            {
                threadHashMap.get(lockKey+"_thread").setLoop(false);
                threadHashMap.remove(lockKey+"_thread");
            }
            redisTemplate.opsForValue().getOperations().delete(lockKey);

            //从锁表中删除这个锁
            redisTemplate.opsForHash().delete(SkylarkRedisLockManagerThread.globalLockTable,lockKey);
        }
    }



}
