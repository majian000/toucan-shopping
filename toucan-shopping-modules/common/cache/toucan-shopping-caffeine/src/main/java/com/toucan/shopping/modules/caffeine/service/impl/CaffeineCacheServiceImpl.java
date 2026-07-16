package com.toucan.shopping.modules.caffeine.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.toucan.shopping.modules.caffeine.service.CaffeineCacheService;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Caffeine 本地缓存实现
 */
public class CaffeineCacheServiceImpl implements CaffeineCacheService {

    private final Cache<Object, Object> cache;

    /**
     * 使用默认配置（写入后 30 分钟过期，最大 10000 条）
     */
    public CaffeineCacheServiceImpl() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .maximumSize(10_000)
                .build();
    }

    /**
     * 自定义过期时间和最大容量
     */
    public CaffeineCacheServiceImpl(long duration, TimeUnit unit, long maxSize) {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(duration, unit)
                .maximumSize(maxSize)
                .build();
    }

    /**
     * 使用自定义 Caffeine 构建器
     */
    public CaffeineCacheServiceImpl(Caffeine<Object, Object> caffeineBuilder) {
        this.cache = caffeineBuilder.build();
    }

    /**
     * 直接注入 Cache 实例
     */
    public CaffeineCacheServiceImpl(Cache<Object, Object> cache) {
        this.cache = cache;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> V get(K key) {
        return (V) cache.getIfPresent(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> V get(K key, Function<K, V> loader) {
        return (V) cache.get(key, k -> loader.apply((K) k));
    }

    @Override
    public <K, V> void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public <K, V> void put(K key, V value, long duration, TimeUnit unit) {
        // Caffeine 不直接支持单条目 TTL，此方法仅写入，过期时间由缓存整体策略控制
        // 如需单条目 TTL，请使用 expireAfter(Expiry) 构建器创建缓存
        cache.put(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> V getIfPresent(K key) {
        return (V) cache.getIfPresent(key);
    }

    @Override
    public <K> void invalidate(K key) {
        cache.invalidate(key);
    }

    @Override
    public <K> void invalidateAll(Iterable<K> keys) {
        cache.invalidateAll(keys);
    }

    @Override
    public void invalidateAll() {
        cache.invalidateAll();
    }

    @Override
    public long size() {
        return cache.estimatedSize();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> asMap() {
        return (Map<K, V>) cache.asMap();
    }

    /**
     * 执行缓存清理
     */
    public void cleanUp() {
        cache.cleanUp();
    }
}
