package com.toucan.shopping.modules.skylark.lock.config;

import com.toucan.shopping.modules.common.util.RenewKeysBucket;
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

/**
 * 锁续期配置,管理2个续期线程的生命周期以及分片桶
 * 每个线程绑定一个分片,key通过hash路由到对应分片,避免重复续期
 */
@Configuration
public class SkylarkLockRenewalConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("skylarkLockRedisTemplate")
    private RedisTemplate redisTemplate;

    /**
     * 分片桶,2个分片对应2个续期线程
     */
    @Bean("skylarkRenewKeysBucket")
    public RenewKeysBucket renewKeysBucket() {
        return new RenewKeysBucket(2);
    }

    private SkylarkRedisLockRenewalThread renewalThread1;
    private SkylarkRedisLockRenewalThread renewalThread2;

    @PostConstruct
    public void init() {
        RenewKeysBucket bucket = renewKeysBucket();

        renewalThread1 = new SkylarkRedisLockRenewalThread();
        renewalThread1.setName("skylark-lock-renewal-1");
        renewalThread1.setShard(bucket.getBucket(0));
        renewalThread1.setRedisTemplate(redisTemplate);
        renewalThread1.start();

        renewalThread2 = new SkylarkRedisLockRenewalThread();
        renewalThread2.setName("skylark-lock-renewal-2");
        renewalThread2.setShard(bucket.getBucket(1));
        renewalThread2.setRedisTemplate(redisTemplate);
        renewalThread2.start();

        logger.info("云雀锁续期线程已启动(分片模式): {}, {}", renewalThread1.getName(), renewalThread2.getName());
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
