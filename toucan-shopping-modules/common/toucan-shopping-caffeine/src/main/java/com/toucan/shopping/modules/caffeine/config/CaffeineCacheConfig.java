package com.toucan.shopping.modules.caffeine.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.toucan.shopping.modules.caffeine.service.CaffeineCacheService;
import com.toucan.shopping.modules.caffeine.service.impl.CaffeineCacheServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine 缓存配置
 *
 * <p>使用时通过 {@code @Qualifier} 指定名称注入：
 * <pre>
 *   {@code @Qualifier("defaultCacheService")}  // 30分钟，10000条
 *   {@code @Qualifier("shortCacheService")}    // 5分钟，5000条
 *   {@code @Qualifier("longCacheService")}     // 2小时，2000条
 * </pre>
 * 不指定 {@code @Qualifier} 时默认注入 {@code defaultCacheService}
 */
@Configuration
public class CaffeineCacheConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String DEFAULT_CACHE = "defaultCacheService";
    public static final String SHORT_CACHE = "shortCacheService";
    public static final String LONG_CACHE = "longCacheService";

    /**
     * 默认本地缓存（30分钟过期，最大10000条）
     */
    @Primary
    @Bean(name = DEFAULT_CACHE)
    public CaffeineCacheService defaultCacheService() {
        logger.info("初始化 Caffeine 默认本地缓存: 30分钟过期, 最大10000条");
        return new CaffeineCacheServiceImpl(30, TimeUnit.MINUTES, 10_000);
    }

    /**
     * 短时效缓存（5分钟过期，最大5000条）
     */
    @Bean(name = SHORT_CACHE)
    public CaffeineCacheService shortCacheService() {
        logger.info("初始化 Caffeine 短时效缓存: 5分钟过期, 最大5000条");
        return new CaffeineCacheServiceImpl(5, TimeUnit.MINUTES, 5_000);
    }

    /**
     * 长时效缓存（2小时过期，最大2000条）
     */
    @Bean(name = LONG_CACHE)
    public CaffeineCacheService longCacheService() {
        logger.info("初始化 Caffeine 长时效缓存: 2小时过期, 最大2000条");
        return new CaffeineCacheServiceImpl(2, TimeUnit.HOURS, 2_000);
    }

    /**
     * 可自定义的原始 Cache 实例（带统计）
     */
    @Bean
    public Cache<Object, Object> caffeineCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .maximumSize(10_000)
                .recordStats()
                .build();
    }
}
