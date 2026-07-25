package com.toucan.shopping.modules.common.lock.redis.config;

import com.toucan.shopping.modules.common.lock.redis.thread.RedisLockRenewalThread;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 锁续期配置,管理2个续期线程的生命周期以及共享的续期key集合
 */
@Configuration
public class RedisLockRenewalConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 共享的续期key集合,暴露为Spring Bean供RedisLockImpl和RedisLockManagerThread使用
     */
    @Bean("renewKeys")
    public ConcurrentHashMap<String, Long> renewKeys() {
        return new ConcurrentHashMap<>();
    }

    private RedisLockRenewalThread renewalThread1;
    private RedisLockRenewalThread renewalThread2;

    @PostConstruct
    public void init() {
        ConcurrentHashMap<String, Long> map = renewKeys();

        renewalThread1 = new RedisLockRenewalThread();
        renewalThread1.setName("redis-lock-renewal-1");
        renewalThread1.setRenewKeys(map);
        renewalThread1.setStringRedisTemplate(stringRedisTemplate);
        renewalThread1.start();

        renewalThread2 = new RedisLockRenewalThread();
        renewalThread2.setName("redis-lock-renewal-2");
        renewalThread2.setRenewKeys(map);
        renewalThread2.setStringRedisTemplate(stringRedisTemplate);
        renewalThread2.start();

        logger.info("锁续期线程已启动: {}, {}", renewalThread1.getName(), renewalThread2.getName());
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
