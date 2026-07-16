package com.toucan.shopping.modules.caffeine.manager;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.toucan.shopping.modules.caffeine.service.CaffeineCacheService;
import com.toucan.shopping.modules.caffeine.service.impl.CaffeineCacheServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Caffeine 多缓存管理器
 * 支持按名称创建和管理多个缓存实例
 */
public class CaffeineCacheManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, CaffeineCacheService> cacheMap = new ConcurrentHashMap<>();

    /**
     * 默认配置
     */
    private long defaultDuration = 30;
    private TimeUnit defaultUnit = TimeUnit.MINUTES;
    private long defaultMaxSize = 10_000;

    private static final class Holder {
        private static final CaffeineCacheManager INSTANCE = new CaffeineCacheManager();
    }

    public static CaffeineCacheManager getInstance() {
        return Holder.INSTANCE;
    }

    private CaffeineCacheManager() {}

    /**
     * 设置默认缓存配置
     */
    public void setDefaultConfig(long duration, TimeUnit unit, long maxSize) {
        this.defaultDuration = duration;
        this.defaultUnit = unit;
        this.defaultMaxSize = maxSize;
    }

    /**
     * 获取或创建指定名称的缓存
     */
    public CaffeineCacheService getCache(String name) {
        return cacheMap.computeIfAbsent(name, k -> {
            logger.info("创建命名缓存: {}, 过期: {}{}, 最大: {}", name, defaultDuration, defaultUnit, defaultMaxSize);
            return new CaffeineCacheServiceImpl(defaultDuration, defaultUnit, defaultMaxSize);
        });
    }

    /**
     * 获取或创建指定名称的缓存（自定义配置）
     */
    public CaffeineCacheService getCache(String name, long duration, TimeUnit unit, long maxSize) {
        return cacheMap.computeIfAbsent(name, k -> {
            logger.info("创建命名缓存: {}, 过期: {}{}, 最大: {}", name, duration, unit, maxSize);
            return new CaffeineCacheServiceImpl(duration, unit, maxSize);
        });
    }

    /**
     * 获取或创建指定名称的缓存（使用 Caffeine 构建器）
     */
    public CaffeineCacheService getCache(String name, Caffeine<Object, Object> builder) {
        return cacheMap.computeIfAbsent(name, k -> {
            logger.info("创建命名缓存: {}, 使用自定义构建器", name);
            return new CaffeineCacheServiceImpl(builder);
        });
    }

    /**
     * 直接获取已有的缓存
     */
    public CaffeineCacheService getCacheIfPresent(String name) {
        return cacheMap.get(name);
    }

    /**
     * 移除指定名称的缓存
     */
    public void removeCache(String name) {
        CaffeineCacheService removed = cacheMap.remove(name);
        if (removed != null) {
            removed.invalidateAll();
            logger.info("移除缓存: {}", name);
        }
    }

    /**
     * 清空所有缓存
     */
    public void clearAll() {
        cacheMap.forEach((name, cache) -> cache.invalidateAll());
        cacheMap.clear();
        logger.info("已清空所有命名缓存");
    }

    /**
     * 获取所有缓存名称
     */
    public java.util.Set<String> cacheNames() {
        return cacheMap.keySet();
    }
}
