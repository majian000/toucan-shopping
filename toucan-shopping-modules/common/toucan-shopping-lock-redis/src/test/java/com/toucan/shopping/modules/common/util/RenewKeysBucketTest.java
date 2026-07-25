package com.toucan.shopping.modules.common.util;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

/**
 * RenewKeysBucket 分片桶单元测试
 */
public class RenewKeysBucketTest {

    @Test
    public void testShardCount() {
        RenewKeysBucket bucket = new RenewKeysBucket(2);
        assertEquals(2, bucket.shardCount());
    }

    @Test
    public void testGetBucketReturnsCorrectShard() {
        RenewKeysBucket bucket = new RenewKeysBucket(2);
        ConcurrentHashMap<String, Long> shard0 = bucket.getBucket(0);
        ConcurrentHashMap<String, Long> shard1 = bucket.getBucket(1);

        assertNotNull(shard0);
        assertNotNull(shard1);
        assertNotSame(shard0, shard1);
    }

    @Test
    public void testPutAndRemoveSameKey() {
        RenewKeysBucket bucket = new RenewKeysBucket(2);
        bucket.put("myLockKey", 30000L);

        // 同一个key的put和remove必须路由到同一分片
        // put后分片里应该有数据
        ConcurrentHashMap<String, Long> shard0 = bucket.getBucket(0);
        ConcurrentHashMap<String, Long> shard1 = bucket.getBucket(1);
        boolean inShard0 = shard0.containsKey("myLockKey");
        boolean inShard1 = shard1.containsKey("myLockKey");

        assertTrue("key should be in exactly one shard", inShard0 ^ inShard1);

        // remove应该从同一分片移除
        bucket.remove("myLockKey");
        assertFalse(shard0.containsKey("myLockKey"));
        assertFalse(shard1.containsKey("myLockKey"));
    }

    @Test
    public void testHashRoutingIsDeterministic() {
        RenewKeysBucket bucket1 = new RenewKeysBucket(2);
        RenewKeysBucket bucket2 = new RenewKeysBucket(2);

        // 相同key必须路由到相同分片索引
        String key = "deterministic-key";
        bucket1.put(key, 1000L);
        bucket2.put(key, 2000L);

        assertEquals(
                "same key must route to same shard across different bucket instances",
                bucket1.getBucket(0).containsKey(key),
                bucket2.getBucket(0).containsKey(key)
        );
    }

    @Test
    public void testKeysSpreadAcrossShards() {
        // 大量key应该均匀分布到两个分片
        RenewKeysBucket bucket = new RenewKeysBucket(2);
        int total = 1000;
        for (int i = 0; i < total; i++) {
            bucket.put("key-" + i, (long) i);
        }

        int shard0Size = bucket.getBucket(0).size();
        int shard1Size = bucket.getBucket(1).size();
        assertEquals(total, shard0Size + shard1Size);

        // 允许10%偏差
        double ratio = (double) shard0Size / shard1Size;
        assertTrue("keys should be roughly balanced, got ratio " + ratio,
                ratio > 0.67 && ratio < 1.5);
    }

    @Test
    public void testRemoveNonExistentKey() {
        RenewKeysBucket bucket = new RenewKeysBucket(2);
        // remove不存在的key不应抛异常
        bucket.remove("no-such-key");
        assertEquals(0, bucket.getBucket(0).size());
        assertEquals(0, bucket.getBucket(1).size());
    }

    @Test
    public void testCustomShardCount() {
        RenewKeysBucket bucket3 = new RenewKeysBucket(3);
        assertEquals(3, bucket3.shardCount());
        assertNotNull(bucket3.getBucket(0));
        assertNotNull(bucket3.getBucket(1));
        assertNotNull(bucket3.getBucket(2));

        RenewKeysBucket bucket4 = new RenewKeysBucket(4);
        assertEquals(4, bucket4.shardCount());
    }
}
