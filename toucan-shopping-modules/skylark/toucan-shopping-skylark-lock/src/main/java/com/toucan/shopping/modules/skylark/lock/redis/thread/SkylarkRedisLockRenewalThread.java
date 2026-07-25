package com.toucan.shopping.modules.skylark.lock.redis.thread;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 锁续期线程(统一管理所有锁的续期,由2个线程实例共享同一个 renewKeys 进行负载分担)
 */
@Data
public class SkylarkRedisLockRenewalThread extends Thread {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 续期间隔(毫秒)
     */
    private long renewalInterval = 10000;

    /**
     * 共享的续期key集合,key为lockKey,value为锁的TTL(毫秒)
     */
    private ConcurrentHashMap<String, Long> renewKeys;

    private RedisTemplate redisTemplate;

    private volatile boolean running = true;

    @Override
    public void run() {
        logger.info("锁续期线程 {} 启动,续期间隔:{}ms", getName(), renewalInterval);
        while (running) {
            try {
                for (Map.Entry<String, Long> entry : renewKeys.entrySet()) {
                    try {
                        Boolean result = redisTemplate.expire(
                                entry.getKey(), entry.getValue(), TimeUnit.MILLISECONDS);
                        // key在Redis中已不存在(已过期或被删除),从续期集合中移除
                        if (result == null || !result) {
                            renewKeys.remove(entry.getKey());
                        }
                    } catch (Exception e) {
                        logger.warn("续期失败 key:{}: {}", entry.getKey(), e.getMessage());
                    }
                }
                Thread.sleep(renewalInterval);
            } catch (InterruptedException e) {
                logger.info("锁续期线程 {} 被中断,退出", getName());
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                logger.warn("锁续期线程 {} 异常: {}", getName(), e.getMessage(), e);
            }
        }
        logger.info("锁续期线程 {} 已退出", getName());
    }

    public void shutdown() {
        this.running = false;
        this.interrupt();
    }
}
