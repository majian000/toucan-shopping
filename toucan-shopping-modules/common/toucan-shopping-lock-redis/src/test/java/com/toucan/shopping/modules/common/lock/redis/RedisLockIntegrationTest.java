package com.toucan.shopping.modules.common.lock.redis;

import com.toucan.shopping.modules.common.lock.redis.impl.RedisLockImpl;
import com.toucan.shopping.modules.common.util.RenewKeysBucket;
import org.junit.jupiter.api.AfterEach;
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
 * 集成测试 —— 连接远程Redis验证分布式锁完整流程(Redis地址见src/test/resources/application.properties)
 * 不需要时注释掉此文件 + test依赖即可: spring-boot-starter-test
 */
@SpringBootTest(classes = TestRedisConfig.class)
public class RedisLockIntegrationTest {

    private static final String TEST_PREFIX = "test:lock:";

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @BeforeEach
    public void setUp() {
        // 仅清理测试相关的key,不flushAll以免影响远程Redis上的其他数据
        deleteTestKeys();
    }

    @AfterEach
    public void tearDown() {
        deleteTestKeys();
    }

    private void deleteTestKeys() {
        Set<String> keys = stringRedisTemplate.keys(TEST_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }
        // 清理global_lock_table中测试相关的entry
        Set<Object> hashKeys = stringRedisTemplate.opsForHash().keys("global_lock_table");
        if (hashKeys != null) {
            for (Object hk : hashKeys) {
                if (hk.toString().startsWith(TEST_PREFIX)) {
                    stringRedisTemplate.opsForHash().delete("global_lock_table", hk);
                }
            }
        }
    }

    // ==================== 基本加锁/解锁 ====================

    @Test
    public void testLockAndUnlock() {
        String key = TEST_PREFIX + "order:1";
        assertTrue(redisLock.lock(key, "uuid-1"));
        assertEquals("uuid-1", stringRedisTemplate.opsForValue().get(key));

        redisLock.unLock(key, "uuid-1");
        assertNull(stringRedisTemplate.opsForValue().get(key));
    }

    @Test
    public void testLockWithCustomTtl() throws Exception {
        String key = TEST_PREFIX + "custom:ttl";
        assertTrue(redisLock.lock(key, "val", 2000L));
        assertNotNull(stringRedisTemplate.opsForValue().get(key));

        Thread.sleep(2500);
        assertNull(stringRedisTemplate.opsForValue().get(key), "key should expire after TTL");
    }

    // ==================== 并发互斥 ====================

    @Test
    public void testLockMutualExclusion() {
        String key = TEST_PREFIX + "mutex";
        ((RedisLockImpl) redisLock).setMaxRetryCount(5);
        ((RedisLockImpl) redisLock).setRetrySleepMs(10);

        assertTrue(redisLock.lock(key, "a"), "first lock should succeed");
        assertFalse(redisLock.lock(key, "b"), "second lock should fail");

        redisLock.unLock(key, "a");
        assertTrue(redisLock.lock(key, "b"), "after unlock, lock should succeed");
        redisLock.unLock(key, "b");

        // 恢复默认
        ((RedisLockImpl) redisLock).setMaxRetryCount(5);
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
        String lockKey = TEST_PREFIX + "concurrent-key";

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
        ((RedisLockImpl) redisLock).setMaxRetryCount(5);
        ((RedisLockImpl) redisLock).setRetrySleepMs(50);
        redisLock.unLock(lockKey, "holder");
    }

    // ==================== 防误删 ====================

    @Test
    public void testUnlockRefusesWrongValue() {
        String key = TEST_PREFIX + "guard-key";
        assertTrue(redisLock.lock(key, "owner-A"));
        redisLock.unLock(key, "owner-B");

        assertEquals("owner-A", stringRedisTemplate.opsForValue().get(key));

        redisLock.unLock(key, "owner-A");
        assertNull(stringRedisTemplate.opsForValue().get(key));
    }

    // ==================== 续期验证 ====================

    @Test
    public void testRenewalExtendsTtl() throws Exception {
        // TTL 15s,续期线程 10s 续期一次,等待12s后应被续期而仍然存活
        String key = TEST_PREFIX + "renew-key";
        assertTrue(redisLock.lock(key, "v", 15000L));
        Thread.sleep(12000);

        assertNotNull(stringRedisTemplate.opsForValue().get(key),
                "key should still exist due to renewal (TTL 15s + 10s renewal cycle)");

        redisLock.unLock(key, "v");
    }

    // ==================== globalLockTable ====================

    @Test
    public void testGlobalLockTableRecordedAndCleaned() {
        String key = TEST_PREFIX + "table-key";
        assertTrue(redisLock.lock(key, "v"));
        Set<Object> keys = stringRedisTemplate.opsForHash().keys("global_lock_table");
        assertTrue(keys.contains(key), "lock should be in global table");

        redisLock.unLock(key, "v");
        keys = stringRedisTemplate.opsForHash().keys("global_lock_table");
        assertFalse(keys.contains(key), "lock should be removed");
    }

    // ==================== RenewKeysBucket ====================

    @Test
    public void testRenewKeysBucketSharding() {
        RenewKeysBucket bucket = new RenewKeysBucket(2);
        for (int i = 0; i < 20; i++) {
            bucket.put(TEST_PREFIX + "key-" + i, 30000L);
        }
        assertEquals(20, bucket.getBucket(0).size() + bucket.getBucket(1).size());
        assertTrue(bucket.getBucket(0).size() > 0, "both shards should be used");
        assertTrue(bucket.getBucket(1).size() > 0, "both shards should be used");

        bucket.remove(TEST_PREFIX + "key-0");
        assertFalse(bucket.getBucket(0).containsKey(TEST_PREFIX + "key-0")
                || bucket.getBucket(1).containsKey(TEST_PREFIX + "key-0"));
    }
}
