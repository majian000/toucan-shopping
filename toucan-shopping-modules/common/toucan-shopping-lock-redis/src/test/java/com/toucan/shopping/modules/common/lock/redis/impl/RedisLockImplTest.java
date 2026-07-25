package com.toucan.shopping.modules.common.lock.redis.impl;

import com.toucan.shopping.modules.common.lock.redis.RedisLock;
import com.toucan.shopping.modules.common.lock.redis.thread.RedisLockManagerThread;
import com.toucan.shopping.modules.common.util.RenewKeysBucket;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * RedisLockImpl 单元测试
 */
public class RedisLockImplTest {

    private StringRedisTemplate stringRedisTemplate;
    private ValueOperations<String, String> valueOperations;
    private HashOperations<String, Object, Object> hashOperations;
    private RenewKeysBucket renewKeysBucket;
    private RedisLockImpl redisLock;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        stringRedisTemplate = mock(StringRedisTemplate.class);
        valueOperations = mock(ValueOperations.class);
        hashOperations = mock(HashOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(stringRedisTemplate.opsForHash()).thenReturn(hashOperations);

        renewKeysBucket = new RenewKeysBucket(2);

        redisLock = new RedisLockImpl();
        redisLock.setStringRedisTemplate(stringRedisTemplate);
        redisLock.setRenewKeysBucket(renewKeysBucket);
    }

    // ========== lock() 测试 ==========

    @Test
    public void testLockSuccess() {
        when(valueOperations.setIfAbsent(eq("order:123"), eq("uuid-1"),
                eq(RedisLock.DEFAULT_MILLISECOND), eq(TimeUnit.MILLISECONDS)))
                .thenReturn(true);

        boolean result = redisLock.lock("order:123", "uuid-1");
        assertTrue("lock should succeed when setIfAbsent returns true", result);

        // 验证key被放入renewKeysBucket和globalLockTable
        verify(hashOperations).put(eq(RedisLockManagerThread.globalLockTable),
                eq("order:123"), anyString());
    }

    @Test
    public void testLockSuccessWithCustomTtl() {
        when(valueOperations.setIfAbsent(eq("custom:key"), eq("val"),
                eq(10000L), eq(TimeUnit.MILLISECONDS)))
                .thenReturn(true);

        boolean result = redisLock.lock("custom:key", "val", 10000L);
        assertTrue("lock should succeed with custom TTL", result);
    }

    @Test
    public void testLockRetriesWhenSetIfAbsentReturnsFalse() {
        // 前两次返回false，第三次返回true
        when(valueOperations.setIfAbsent(anyString(), anyString(), anyLong(), any()))
                .thenReturn(false, false, true);

        boolean result = redisLock.lock("contended-key", "val");
        assertTrue("lock should eventually succeed after retries", result);

        // 验证至少调用了3次
        verify(valueOperations, atLeast(3)).setIfAbsent(
                eq("contended-key"), eq("val"), anyLong(), any());
    }

    @Test
    public void testLockGivesUpAfterMaxRetries() {
        // 极小的重试参数,加速测试
        redisLock.setMaxRetryCount(5);
        redisLock.setRetrySleepMs(1);

        // 始终返回false（锁一直被占用）
        when(valueOperations.setIfAbsent(anyString(), anyString(), anyLong(), any()))
                .thenReturn(false);

        boolean result = redisLock.lock("hot-key", "val");
        assertFalse("lock should fail after exhausting retries", result);

        // verify globalLockTable never called
        verify(hashOperations, never()).put(any(), any(), any());
    }

    @Test
    public void testLockPutsKeyIntoRenewKeysBucket() {
        when(valueOperations.setIfAbsent(eq("bucket-key"), eq("v"),
                eq(30000L), eq(TimeUnit.MILLISECONDS)))
                .thenReturn(true);

        redisLock.lock("bucket-key", "v", 30000L);
        assertEquals(1, renewKeysBucket.getBucket(0).size() + renewKeysBucket.getBucket(1).size());
    }

    @Test
    public void testDefaultTtl() {
        when(valueOperations.setIfAbsent(eq("default-key"), eq("v"),
                eq(RedisLock.DEFAULT_MILLISECOND), eq(TimeUnit.MILLISECONDS)))
                .thenReturn(true);

        boolean result = redisLock.lock("default-key", "v");
        assertTrue(result);
        assertEquals(30000L, RedisLock.DEFAULT_MILLISECOND);
    }

    // ========== unLock() 测试 ==========

    @Test
    public void testUnlockSuccess() {
        when(valueOperations.get(eq("my-lock"))).thenReturn("my-value");
        when(valueOperations.getOperations()).thenReturn(stringRedisTemplate);

        redisLock.unLock("my-lock", "my-value");

        verify(stringRedisTemplate).delete("my-lock");
        verify(hashOperations).delete(RedisLockManagerThread.globalLockTable, "my-lock");
    }

    @Test
    public void testUnlockRefusesWhenValueMismatch() {
        // 锁的值不匹配——防止误删别人的锁
        when(valueOperations.get(eq("my-lock"))).thenReturn("other-value");

        redisLock.unLock("my-lock", "my-value");

        verify(stringRedisTemplate, never()).delete(anyString());
        verify(hashOperations, never()).delete(any(), any());
    }

    @Test
    public void testUnlockCleansUpWhenKeyAlreadyExpired() {
        // key已经过期，redisLockValue为null
        when(valueOperations.get(eq("expired-key"))).thenReturn(null);
        when(valueOperations.getOperations()).thenReturn(stringRedisTemplate);

        // 不应抛异常
        redisLock.unLock("expired-key", "my-value");

        // 仍然尝试删除（清理残留）
        verify(stringRedisTemplate).delete("expired-key");
        verify(hashOperations).delete(RedisLockManagerThread.globalLockTable, "expired-key");
    }

    @Test
    public void testUnlockRemovesFromRenewKeysBucket() {
        when(valueOperations.get(eq("remove-key"))).thenReturn("val");
        when(valueOperations.getOperations()).thenReturn(stringRedisTemplate);

        // 先加锁放入bucket
        when(valueOperations.setIfAbsent(eq("remove-key"), eq("val"), anyLong(), any()))
                .thenReturn(true);
        redisLock.lock("remove-key", "val");
        int before = renewKeysBucket.getBucket(0).size() + renewKeysBucket.getBucket(1).size();
        assertEquals(1, before);

        // 解锁后从bucket移除
        redisLock.unLock("remove-key", "val");
        int after = renewKeysBucket.getBucket(0).size() + renewKeysBucket.getBucket(1).size();
        assertEquals(0, after);
    }

    // ========== renewKeysBucket 联动测试 ==========

    @Test
    public void testMultipleLockKeysSpreadAcrossShards() {
        when(valueOperations.setIfAbsent(anyString(), anyString(), anyLong(), any()))
                .thenReturn(true);

        for (int i = 0; i < 10; i++) {
            redisLock.lock("key-" + i, "val-" + i);
        }

        int shard0 = renewKeysBucket.getBucket(0).size();
        int shard1 = renewKeysBucket.getBucket(1).size();
        assertEquals(10, shard0 + shard1);
        assertTrue("both shards should have entries", shard0 > 0 && shard1 > 0);
    }
}
