package com.toucan.shopping.modules.common.lock.redis.thread;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 锁续期线程,绑定一个分片桶,只续期该分片内的key
 */
@Data
public class RedisLockRenewalThread extends Thread {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 续期间隔(毫秒)
     */
    private long renewalInterval = 10000;

    /**
     * 本线程绑定的分片,只续期这个分片内的key
     */
    private ConcurrentHashMap<String, Long> shard;

    private StringRedisTemplate stringRedisTemplate;

    private volatile boolean running = true;

    @Override
    public void run() {
        logger.info("锁续期线程 {} 启动,续期间隔:{}ms", getName(), renewalInterval);
        while (running) {
            try {
                for (Map.Entry<String, Long> entry : shard.entrySet()) {
                    try {
                        Boolean result = stringRedisTemplate.expire(
                                entry.getKey(), entry.getValue(), TimeUnit.MILLISECONDS);
                        // key在Redis中已不存在(已过期或被删除),从分片中移除
                        if (result == null || !result) {
                            shard.remove(entry.getKey());
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
