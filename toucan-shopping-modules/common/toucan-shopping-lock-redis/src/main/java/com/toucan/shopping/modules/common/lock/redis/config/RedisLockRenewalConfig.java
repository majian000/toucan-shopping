package com.toucan.shopping.modules.common.lock.redis.config;

import com.toucan.shopping.modules.common.lock.redis.thread.RedisLockRenewalThread;
import com.toucan.shopping.modules.common.util.RenewKeysBucket;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 锁续期配置,管理2个续期线程的生命周期以及分片桶
 * 每个线程绑定一个分片,key通过hash路由到对应分片,避免重复续期
 */
@Configuration
public class RedisLockRenewalConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 分片桶,2个分片对应2个续期线程
     */
    @Bean("renewKeysBucket")
    public RenewKeysBucket renewKeysBucket() {
        return new RenewKeysBucket(2);
    }

    private RedisLockRenewalThread renewalThread1;
    private RedisLockRenewalThread renewalThread2;

    @PostConstruct
    public void init() {
        RenewKeysBucket bucket = renewKeysBucket();

        renewalThread1 = new RedisLockRenewalThread();
        renewalThread1.setName("redis-lock-renewal-1");
        renewalThread1.setShard(bucket.getBucket(0));
        renewalThread1.setStringRedisTemplate(stringRedisTemplate);
        renewalThread1.start();

        renewalThread2 = new RedisLockRenewalThread();
        renewalThread2.setName("redis-lock-renewal-2");
        renewalThread2.setShard(bucket.getBucket(1));
        renewalThread2.setStringRedisTemplate(stringRedisTemplate);
        renewalThread2.start();

        logger.info("锁续期线程已启动(分片模式): {}, {}", renewalThread1.getName(), renewalThread2.getName());
    }

    @PreDestroy
    public void destroy() {
        if (renewalThread1 != null) {
            renewalThread1.shutdown();
        }
        if (renewalThread2 != null) {
            renewalThread2.shutdown();
        }
        logger.info("锁续期线程已关闭");
    }
}
