package com.toucan.shopping.modules.caffeine.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Caffeine 本地缓存通用操作接口
 */
public interface CaffeineCacheService {

    /**
     * 获取缓存，不存在则返回 null
     */
    <K, V> V get(K key);

    /**
     * 获取缓存，不存在则调用 loader 加载并缓存
     */
    <K, V> V get(K key, Function<K, V> loader);

    /**
     * 存入缓存（使用默认过期时间）
     */
    <K, V> void put(K key, V value);

    /**
     * 存入缓存并指定过期时间
     */
    <K, V> void put(K key, V value, long duration, TimeUnit unit);

    /**
     * 如果不存在则存入（原子操作）
     */
    <K, V> V getIfPresent(K key);

    /**
     * 删除指定 key
     */
    <K> void invalidate(K key);

    /**
     * 批量删除
     */
    <K> void invalidateAll(Iterable<K> keys);

    /**
     * 清空所有缓存
     */
    void invalidateAll();

    /**
     * 获取缓存数量
     */
    long size();

    /**
     * 获取所有缓存条目
     */
    <K, V> Map<K, V> asMap();

}
