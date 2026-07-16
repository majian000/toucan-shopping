package com.toucan.shopping.modules.caffeine.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Caffeine 缓存快捷构建工具
 */
public final class CaffeineUtils {

    private CaffeineUtils() {}

    /**
     * 创建一个简单缓存（写入后过期）
     */
    public static <K, V> Cache<K, V> newCache(long duration, TimeUnit unit, long maxSize) {
        return Caffeine.newBuilder()
                .expireAfterWrite(duration, unit)
                .maximumSize(maxSize)
                .build();
    }

    /**
     * 创建一个简单缓存（访问后过期）
     */
    public static <K, V> Cache<K, V> newCacheAfterAccess(long duration, TimeUnit unit, long maxSize) {
        return Caffeine.newBuilder()
                .expireAfterAccess(duration, unit)
                .maximumSize(maxSize)
                .build();
    }

    /**
     * 创建一个自动加载缓存
     */
    public static <K, V> LoadingCache<K, V> newLoadingCache(long duration, TimeUnit unit, long maxSize, Function<K, V> loader) {
        return Caffeine.newBuilder()
                .expireAfterWrite(duration, unit)
                .maximumSize(maxSize)
                .build(loader::apply);
    }

    /**
     * 创建一个短时效缓存（5分钟）
     */
    public static <K, V> Cache<K, V> newShortCache(long maxSize) {
        return newCache(5, TimeUnit.MINUTES, maxSize);
    }

    /**
     * 创建一个标准时效缓存（30分钟）
     */
    public static <K, V> Cache<K, V> newStandardCache(long maxSize) {
        return newCache(30, TimeUnit.MINUTES, maxSize);
    }

    /**
     * 创建一个长时效缓存（2小时）
     */
    public static <K, V> Cache<K, V> newLongCache(long maxSize) {
        return newCache(2, TimeUnit.HOURS, maxSize);
    }
}
