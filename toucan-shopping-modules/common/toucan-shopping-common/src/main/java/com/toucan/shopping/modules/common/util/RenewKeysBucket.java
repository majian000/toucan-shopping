package com.toucan.shopping.modules.common.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 锁续期key分片桶,通过hash将key路由到不同分片,
 * 每个续期线程绑定一个分片,避免多线程重复续期同一个key
 */
public class RenewKeysBucket {

    private final ConcurrentHashMap<String, Long>[] buckets;

    public RenewKeysBucket(int shardCount) {
        this.buckets = new ConcurrentHashMap[shardCount];
        for (int i = 0; i < shardCount; i++) {
            buckets[i] = new ConcurrentHashMap<>();
        }
    }

    /**
     * 将key加入对应分片
     */
    public void put(String key, Long ttl) {
        bucket(key).put(key, ttl);
    }

    /**
     * 从对应分片移除key
     */
    public void remove(String key) {
        bucket(key).remove(key);
    }

    /**
     * 获取指定分片的map,供续期线程绑定使用
     */
    public ConcurrentHashMap<String, Long> getBucket(int idx) {
        return buckets[idx];
    }

    public int shardCount() {
        return buckets.length;
    }

    private ConcurrentHashMap<String, Long> bucket(String key) {
        int idx = Math.abs(key.hashCode()) % buckets.length;
        return buckets[idx];
    }
}
