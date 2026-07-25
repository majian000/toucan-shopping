package com.toucan.shopping.modules.common.lock.redis.thread;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 锁续期线程,绑定一个分片桶,只续期该分片内的key
 */
public class RedisLockRenewalThread extends Thread {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 续期间隔(毫秒)
     */
    @Setter
    private long renewalInterval = 10000;

    /**
     * 本线程绑定的分片,只续期这个分片内的key
     */
    @Setter
    private ConcurrentHashMap<String, Long> shard;

    @Setter
    private StringRedisTemplate stringRedisTemplate;

    private volatile boolean running = true;

    @Override
    public void run() {
        logger.info("锁续期线程 {} 启动,续期间隔:{}ms", getName(), renewalInterval);
        while (running) {
            try {
                // 收集需要移除的key,迭代结束后统一移除,避免ConcurrentHashMap迭代中途直接remove导致跳过条目
                List<String> expiredKeys = new ArrayList<>();
                for (Map.Entry<String, Long> entry : shard.entrySet()) {
                    try {
                        Boolean result = stringRedisTemplate.expire(
                                entry.getKey(), entry.getValue(), TimeUnit.MILLISECONDS);
                        if (result == null || !result) {
                            expiredKeys.add(entry.getKey());
                        }
                    } catch (Exception e) {
                        logger.warn("续期失败 key:{}: {}", entry.getKey(), e.getMessage());
                        expiredKeys.add(entry.getKey());
                    }
                }
                for (String key : expiredKeys) {
                    shard.remove(key);
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
