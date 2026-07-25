package com.toucan.shopping.modules.skylark.lock.config;

import com.toucan.shopping.modules.skylark.lock.redis.thread.SkylarkRedisLockRenewalThread;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 锁续期配置,管理2个续期线程的生命周期以及共享的续期key集合
 */
@Configuration
public class SkylarkLockRenewalConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("skylarkLockRedisTemplate")
    private RedisTemplate redisTemplate;

    /**
     * 共享的续期key集合,暴露为Spring Bean供SkylarkRedisLockImpl和SkylarkRedisLockManagerThread使用
     */
    @Bean("skylarkRenewKeys")
    public ConcurrentHashMap<String, Long> renewKeys() {
        return new ConcurrentHashMap<>();
    }

    private SkylarkRedisLockRenewalThread renewalThread1;
    private SkylarkRedisLockRenewalThread renewalThread2;

    @PostConstruct
    public void init() {
        ConcurrentHashMap<String, Long> map = renewKeys();

        renewalThread1 = new SkylarkRedisLockRenewalThread();
        renewalThread1.setName("skylark-lock-renewal-1");
        renewalThread1.setRenewKeys(map);
        renewalThread1.setRedisTemplate(redisTemplate);
        renewalThread1.start();

        renewalThread2 = new SkylarkRedisLockRenewalThread();
        renewalThread2.setName("skylark-lock-renewal-2");
        renewalThread2.setRenewKeys(map);
        renewalThread2.setRedisTemplate(redisTemplate);
        renewalThread2.start();

        logger.info("云雀锁续期线程已启动: {}, {}", renewalThread1.getName(), renewalThread2.getName());
    }

    @PreDestroy
    public void destroy() {
        if (renewalThread1 != null) {
            renewalThread1.shutdown();
        }
        if (renewalThread2 != null) {
            renewalThread2.shutdown();
        }
        logger.info("云雀锁续期线程已关闭");
    }
}
