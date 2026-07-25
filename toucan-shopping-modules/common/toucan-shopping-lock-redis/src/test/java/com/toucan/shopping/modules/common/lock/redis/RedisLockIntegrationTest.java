package com.toucan.shopping.modules.common.lock.redis;

import com.toucan.shopping.modules.common.lock.redis.impl.RedisLockImpl;
import com.toucan.shopping.modules.common.util.RenewKeysBucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 集成测试 —— 内嵌Redis验证分布式锁完整流程(TestRedisConfig负责Redis启停及端口配置)
 * 不需要时注释掉此文件 + 2个test依赖即可: spring-boot-starter-test / embedded-redis
 */
@SpringBootTest(classes = TestRedisConfig.class)
public class RedisLockIntegrationTest {

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @BeforeEach
    public void setUp() {
        stringRedisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    // ==================== 基本加锁/解锁 ====================

    @Test
    public void testLockAndUnlock() {
        assertTrue(redisLock.lock("order:1", "uuid-1"));
        assertEquals("uuid-1", stringRedisTemplate.opsForValue().get("order:1"));

        redisLock.unLock("order:1", "uuid-1");
        assertNull(stringRedisTemplate.opsForValue().get("order:1"));
    }

    @Test
    public void testLockWithCustomTtl() throws Exception {
        assertTrue(redisLock.lock("custom:ttl", "val", 2000L));
        assertNotNull(stringRedisTemplate.opsForValue().get("custom:ttl"));

        Thread.sleep(2500);
        assertNull(stringRedisTemplate.opsForValue().get("custom:ttl"), "key should expire after TTL");
    }

    // ==================== 并发互斥 ====================

    @Test
    public void testLockMutualExclusion() {
        ((RedisLockImpl) redisLock).setMaxRetryCount(5);
        ((RedisLockImpl) redisLock).setRetrySleepMs(10);

        assertTrue(redisLock.lock("mutex", "a"), "first lock should succeed");
        assertFalse(redisLock.lock("mutex", "b"), "second lock should fail");

        redisLock.unLock("mutex", "a");
        assertTrue(redisLock.lock("mutex", "b"), "after unlock, lock should succeed");
        redisLock.unLock("mutex", "b");

        // 恢复默认
        ((RedisLockImpl) redisLock).setMaxRetryCount(RedisLock.DEFAULT_TRY_COUNT);
        ((RedisLockImpl) redisLock).setRetrySleepMs(50);
    }

    @Test
    public void testConcurrentLock() throws Exception {
        // 减少重试次数,加速并发竞争
        ((RedisLockImpl) redisLock).setMaxRetryCount(5);
        ((RedisLockImpl) redisLock).setRetrySleepMs(10);

        int threads = 4;
        CountDownLatch latch = new CountDownLatch(threads);
        int[] successCount = {0};
        String lockKey = "concurrent-key";

        // 主线程先占锁
        assertTrue(redisLock.lock(lockKey, "holder"));

        // 其他线程竞争同一把锁,都应失败(锁被主线程持有)
        for (int i = 0; i < threads; i++) {
            new Thread(() -> {
                if (redisLock.lock(lockKey, "thread-" + Thread.currentThread().getId())) {
                    successCount[0]++;
                }
                latch.countDown();
            }).start();
        }

        latch.await(5, TimeUnit.SECONDS);
        assertEquals(0, successCount[0], "no other thread should acquire while lock held");

        // 恢复默认值
        ((RedisLockImpl) redisLock).setMaxRetryCount(RedisLock.DEFAULT_TRY_COUNT);
        ((RedisLockImpl) redisLock).setRetrySleepMs(50);
        redisLock.unLock(lockKey, "holder");
    }

    // ==================== 防误删 ====================

    @Test
    public void testUnlockRefusesWrongValue() {
        assertTrue(redisLock.lock("guard-key", "owner-A"));
        redisLock.unLock("guard-key", "owner-B");

        assertEquals("owner-A", stringRedisTemplate.opsForValue().get("guard-key"));

        redisLock.unLock("guard-key", "owner-A");
        assertNull(stringRedisTemplate.opsForValue().get("guard-key"));
    }

    // ==================== 续期验证 ====================

    @Test
    public void testRenewalExtendsTtl() throws Exception {
        // TTL 15s,续期线程 10s 续期一次,等待12s后应被续期而仍然存活
        assertTrue(redisLock.lock("renew-key", "v", 15000L));
        Thread.sleep(12000);

        assertNotNull(stringRedisTemplate.opsForValue().get("renew-key"),
                "key should still exist due to renewal (TTL 15s + 10s renewal cycle)");

        redisLock.unLock("renew-key", "v");
    }

    // ==================== globalLockTable ====================

    @Test
    public void testGlobalLockTableRecordedAndCleaned() {
        assertTrue(redisLock.lock("table-key", "v"));
        Set<Object> keys = stringRedisTemplate.opsForHash().keys("global_lock_table");
        assertTrue(keys.contains("table-key"), "lock should be in global table");

        redisLock.unLock("table-key", "v");
        keys = stringRedisTemplate.opsForHash().keys("global_lock_table");
        assertFalse(keys.contains("table-key"), "lock should be removed");
    }

    // ==================== RenewKeysBucket ====================

    @Test
    public void testRenewKeysBucketSharding() {
        RenewKeysBucket bucket = new RenewKeysBucket(2);
        for (int i = 0; i < 20; i++) {
            bucket.put("key-" + i, 30000L);
        }
        assertEquals(20, bucket.getBucket(0).size() + bucket.getBucket(1).size());
        assertTrue(bucket.getBucket(0).size() > 0, "both shards should be used");
        assertTrue(bucket.getBucket(1).size() > 0, "both shards should be used");

        bucket.remove("key-0");
        assertFalse(bucket.getBucket(0).containsKey("key-0")
                || bucket.getBucket(1).containsKey("key-0"));
    }
}
