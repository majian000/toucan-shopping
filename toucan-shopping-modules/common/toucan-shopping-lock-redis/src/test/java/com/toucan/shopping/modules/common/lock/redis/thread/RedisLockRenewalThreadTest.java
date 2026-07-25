package com.toucan.shopping.modules.common.lock.redis.thread;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * RedisLockRenewalThread 单元测试
 */
public class RedisLockRenewalThreadTest {

    private StringRedisTemplate stringRedisTemplate;
    private ValueOperations<String, String> valueOperations;
    private ConcurrentHashMap<String, Long> shard;
    private RedisLockRenewalThread thread;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        stringRedisTemplate = mock(StringRedisTemplate.class);
        valueOperations = mock(ValueOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);

        shard = new ConcurrentHashMap<>();

        thread = new RedisLockRenewalThread();
        thread.setName("test-renewal-thread");
        thread.setShard(shard);
        thread.setStringRedisTemplate(stringRedisTemplate);
        thread.setRenewalInterval(50); // 缩短间隔加速测试
    }

    @After
    public void tearDown() {
        thread.shutdown();
    }

    @Test
    public void testRenewsKeysInShard() throws Exception {
        shard.put("key-a", 30000L);
        shard.put("key-b", 60000L);
        when(valueOperations.getOperations()).thenReturn(null);
        when(stringRedisTemplate.expire(eq("key-a"), eq(30000L), eq(TimeUnit.MILLISECONDS)))
                .thenReturn(true);
        when(stringRedisTemplate.expire(eq("key-b"), eq(60000L), eq(TimeUnit.MILLISECONDS)))
                .thenReturn(true);

        thread.start();
        Thread.sleep(150); // 等待至少一次续期周期
        thread.shutdown();
        thread.join(2000);

        verify(stringRedisTemplate, atLeastOnce()).expire(eq("key-a"), eq(30000L), eq(TimeUnit.MILLISECONDS));
        verify(stringRedisTemplate, atLeastOnce()).expire(eq("key-b"), eq(60000L), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    public void testRemovesKeyWhenExpireReturnsFalse() throws Exception {
        shard.put("stale-key", 5000L);
        when(stringRedisTemplate.expire(eq("stale-key"), eq(5000L), eq(TimeUnit.MILLISECONDS)))
                .thenReturn(false);

        thread.start();
        Thread.sleep(150);
        thread.shutdown();
        thread.join(2000);

        // stale key应该被自动清理
        assertFalse("stale key should be removed when expire returns false",
                shard.containsKey("stale-key"));
    }

    @Test
    public void testRemovesKeyWhenExpireReturnsNull() throws Exception {
        shard.put("null-key", 5000L);
        when(stringRedisTemplate.expire(eq("null-key"), eq(5000L), eq(TimeUnit.MILLISECONDS)))
                .thenReturn(null);

        thread.start();
        Thread.sleep(150);
        thread.shutdown();
        thread.join(2000);

        assertFalse("key should be removed when expire returns null",
                shard.containsKey("null-key"));
    }

    @Test
    public void testShutdownStopsThread() throws Exception {
        thread.start();
        assertTrue(thread.isAlive());

        thread.shutdown();
        thread.join(2000);

        assertFalse("thread should be dead after shutdown", thread.isAlive());
    }

    @Test
    public void testDoesNotThrowOnEmptyShard() throws Exception {
        // 分片为空时续期不应抛异常
        thread.start();
        Thread.sleep(100);
        thread.shutdown();
        thread.join(2000);
        // 没有异常就是通过
    }

    @Test
    public void testContinuesAfterExpireException() throws Exception {
        shard.put("good-key", 30000L);
        shard.put("bad-key", 5000L);
        when(stringRedisTemplate.expire(eq("bad-key"), anyLong(), any()))
                .thenThrow(new RuntimeException("redis connection error"));
        when(stringRedisTemplate.expire(eq("good-key"), eq(30000L), eq(TimeUnit.MILLISECONDS)))
                .thenReturn(true);

        thread.start();
        Thread.sleep(150);
        thread.shutdown();
        thread.join(2000);

        // bad-key抛异常不影响good-key续期
        verify(stringRedisTemplate, atLeastOnce()).expire(eq("good-key"), eq(30000L), eq(TimeUnit.MILLISECONDS));
        // bad-key仍然在分片中（续期失败不应删除）
        assertTrue("bad-key should remain after failed renew", shard.containsKey("bad-key"));
    }
}
