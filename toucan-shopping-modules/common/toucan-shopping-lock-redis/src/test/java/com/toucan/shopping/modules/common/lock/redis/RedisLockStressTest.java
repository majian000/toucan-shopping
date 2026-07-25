package com.toucan.shopping.modules.common.lock.redis;

import com.toucan.shopping.modules.common.lock.redis.bucket.RenewKeysBucket;
import com.toucan.shopping.modules.common.lock.redis.thread.RedisLockManagerThread;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Redis分布式锁高并发压力测试
 * <p>
 * 运行: mvn test -Dtest=com.toucan.shopping.modules.common.lock.redis.RedisLockStressTest
 * -pl toucan-shopping-modules/common/toucan-shopping-lock-redis
 */
@SpringBootTest(classes = TestRedisConfig.class)
public class RedisLockStressTest {

    private static final Logger log = LoggerFactory.getLogger(RedisLockStressTest.class);
    private static final String PREFIX = "stress:lock:";

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RenewKeysBucket renewKeysBucket;

    private static long savedLockTimeout;
    private static long savedManagerInterval;

    @BeforeAll
    public static void setUp() {
        savedLockTimeout = RedisLockManagerThread.lockTimeOutMillisecond;
        savedManagerInterval = RedisLockManagerThread.redisManagerExecMillisecond;
        RedisLockManagerThread.lockTimeOutMillisecond = 10_000;
        RedisLockManagerThread.redisManagerExecMillisecond = 2_000;

        log.info("╔══════════════════════════════════════════════╗");
        log.info("║  Redis分布式锁 高并发压力测试                    ║");
        log.info("╠══════════════════════════════════════════════╣");
        log.info("║  管理线程: 扫描间隔={}ms, 超时阈值={}ms        ║",
                RedisLockManagerThread.redisManagerExecMillisecond,
                RedisLockManagerThread.lockTimeOutMillisecond);
        log.info("║  续期线程: interval=10s, shards=2            ║");
        log.info("╚══════════════════════════════════════════════╝");
    }

    @AfterAll
    public static void tearDown() {
        RedisLockManagerThread.lockTimeOutMillisecond = savedLockTimeout;
        RedisLockManagerThread.redisManagerExecMillisecond = savedManagerInterval;
        log.info("========== 全部测试完成, 配置已恢复 ==========");
    }

    @Test
    public void testAll() throws Exception {
        scenario1MutualExclusion();
        cleanup();

        scenario2HighFrequency();
        cleanup();

        scenario3MultiKeySharding();
        cleanup();

        scenario4Renewal();
        cleanup();

        scenario5WrongValueUnlock();
        cleanup();

        scenario6TimeoutCleanup();
        cleanup();

        scenario7MixedPressure();
        cleanup();

        scenario8ConcurrentUnlockRace();
        cleanup();

        scenario9BucketMemoryCheck();
        cleanup();

        log.info("╔══════════════════════════════════════════════╗");
        log.info("║  全部9个场景执行完毕                            ║");
        log.info("╚══════════════════════════════════════════════╝");
    }

    // ============================================================
    // helpers
    // ============================================================

    private void cleanup() {
        Set<String> keys = stringRedisTemplate.keys(PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }
        Set<Object> hashKeys = stringRedisTemplate.opsForHash().keys(RedisLockManagerThread.globalLockTable);
        if (hashKeys != null) {
            for (Object hk : hashKeys) {
                if (hk.toString().startsWith(PREFIX)) {
                    stringRedisTemplate.opsForHash().delete(RedisLockManagerThread.globalLockTable, hk);
                }
            }
        }
        for (int i = 0; i < renewKeysBucket.shardCount(); i++) {
            renewKeysBucket.getBucket(i).keySet().removeIf(k -> k.startsWith(PREFIX));
        }
    }

    // ============================================================
    // 场景1: 基本互斥 — 50线程抢同一把锁
    // ============================================================

    private void scenario1MutualExclusion() throws Exception {
        log.info("┌──────────────────────────────────────────────┐");
        log.info("│  场景1: 基本互斥 — 50线程抢同一把锁 (每线程5轮)    │");
        log.info("└──────────────────────────────────────────────┘");

        final String lockKey = PREFIX + "mutex";
        final int threads = 50;
        final int rounds = 5;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch gate = new CountDownLatch(1);
        AtomicInteger inSection = new AtomicInteger(0);
        AtomicInteger violations = new AtomicInteger(0);
        AtomicInteger lockOk = new AtomicInteger(0);
        AtomicInteger lockFail = new AtomicInteger(0);

        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            final int id = i;
            futures.add(pool.submit(() -> {
                try { gate.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
                String v = "t-" + id;
                for (int r = 0; r < rounds; r++) {
                    if (redisLock.lock(lockKey, v, 30_000L)) {
                        int n = inSection.incrementAndGet();
                        if (n > 1) violations.incrementAndGet();
                        lockOk.incrementAndGet();
                        try { Thread.sleep(ThreadLocalRandom.current().nextInt(1, 6)); }
                        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                        inSection.decrementAndGet();
                        redisLock.unLock(lockKey, v);
                    } else {
                        lockFail.incrementAndGet();
                    }
                }
            }));
        }

        long t0 = System.currentTimeMillis();
        gate.countDown();
        for (Future<?> f : futures) f.get(60, TimeUnit.SECONDS);
        long elapsed = System.currentTimeMillis() - t0;
        pool.shutdown();

        log.info("[场景1结果] 尝试={}, 成功={}, 失败={}, 违规={}, 耗时={}ms",
                lockOk.get() + lockFail.get(), lockOk.get(), lockFail.get(), violations.get(), elapsed);
        assertEquals(0, violations.get(), "场景1互斥违规");
        assertTrue(lockOk.get() > 0, "至少有一次成功加锁");
    }

    // ============================================================
    // 场景2: 高频加解锁 — 20线程各100次
    // ============================================================

    private void scenario2HighFrequency() throws Exception {
        log.info("┌──────────────────────────────────────────────┐");
        log.info("│  场景2: 高频加解锁 — 20线程各100次(独立key)      │");
        log.info("└──────────────────────────────────────────────┘");

        final int threads = 20;
        final int ops = 100;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch done = new CountDownLatch(threads);
        AtomicInteger ok = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);
        LongAdder lockNs = new LongAdder();

        long t0 = System.currentTimeMillis();
        for (int i = 0; i < threads; i++) {
            final int id = i;
            pool.submit(() -> {
                String key = PREFIX + "hf-" + id;
                String v = "v-" + id;
                for (int j = 0; j < ops; j++) {
                    long t = System.nanoTime();
                    if (redisLock.lock(key, v, 30_000L)) {
                        lockNs.add(System.nanoTime() - t);
                        ok.incrementAndGet();
                        redisLock.unLock(key, v);
                    } else {
                        fail.incrementAndGet();
                    }
                }
                done.countDown();
            });
        }
        done.await(120, TimeUnit.SECONDS);
        long elapsed = System.currentTimeMillis() - t0;
        pool.shutdown();

        double avgUs = ok.get() > 0 ? lockNs.sum() / (double) ok.get() / 1000.0 : 0;
        log.info("[场景2结果] 操作={}, 成功={}, 失败={}, 耗时={}ms, 吞吐={}/s, 平均加锁={}μs",
                ok.get() + fail.get(), ok.get(), fail.get(), elapsed,
                String.format("%.0f", (ok.get() + fail.get()) * 1000.0 / elapsed),
                String.format("%.1f", avgUs));
        assertEquals(threads * ops, ok.get(), "独立key应全部成功");
    }

    // ============================================================
    // 场景3: 多Key并行 + 分片分布验证
    // ============================================================

    private void scenario3MultiKeySharding() throws Exception {
        log.info("┌──────────────────────────────────────────────┐");
        log.info("│  场景3: 多Key并行 + 分片分布验证                 │");
        log.info("└──────────────────────────────────────────────┘");

        final int threads = 30;
        final int keys = 5;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch done = new CountDownLatch(threads);
        ConcurrentHashMap<String, String> acquired = new ConcurrentHashMap<>();

        long t0 = System.currentTimeMillis();
        for (int i = 0; i < threads; i++) {
            final int id = i;
            pool.submit(() -> {
                Random rand = new Random();
                for (int k = 0; k < keys; k++) {
                    String key = PREFIX + "multi-" + id + "-" + k;
                    String v = UUID.randomUUID().toString().substring(0, 8);
                    if (redisLock.lock(key, v, 30_000L)) {
                        acquired.put(key, v);
                        try { Thread.sleep(rand.nextInt(41) + 10); }
                        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    }
                }
                done.countDown();
            });
        }
        done.await(120, TimeUnit.SECONDS);
        long elapsed = System.currentTimeMillis() - t0;
        pool.shutdown();

        int unlocked = 0;
        for (Map.Entry<String, String> e : acquired.entrySet()) {
            redisLock.unLock(e.getKey(), e.getValue());
            unlocked++;
        }

        int s0 = renewKeysBucket.getBucket(0).size();
        int s1 = renewKeysBucket.getBucket(1).size();
        log.info("[场景3结果] 加锁={}, 解锁={}, 耗时={}ms, shard0={}, shard1={}",
                acquired.size(), unlocked, elapsed, s0, s1);
        assertEquals(threads * keys, acquired.size(), "全部应成功加锁");
        assertTrue(s0 <= 0, "shard0残留=" + s0);
        assertTrue(s1 <= 0, "shard1残留=" + s1);
    }

    // ============================================================
    // 场景4: 续期验证 — 持锁35s, TTL 30s
    // ============================================================

    private void scenario4Renewal() throws Exception {
        log.info("┌──────────────────────────────────────────────┐");
        log.info("│  场景4: 续期验证 — TTL=30s, 持锁35s             │");
        log.info("└──────────────────────────────────────────────┘");

        final String key = PREFIX + "renewal";
        final String v = "renewal-test";
        assertTrue(redisLock.lock(key, v, 30_000L), "加锁应成功");

        Long initTtl = stringRedisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
        log.info("[场景4] 加锁成功, 初始TTL={}ms", initTtl);

        boolean alive = true;
        for (int i = 5; i <= 35; i += 5) {
            Thread.sleep(5_000);
            Long ttl = stringRedisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
            log.info("[场景4] 持锁{}s, TTL={}ms", i, ttl);
            if (ttl == null || ttl <= 0) { alive = false; break; }
        }

        String finalVal = stringRedisTemplate.opsForValue().get(key);
        log.info("[场景4] 最终: key存在={}, value匹配={}", finalVal != null, v.equals(finalVal));
        assertTrue(alive, "续期线程应保持锁存活");
        assertEquals(v, finalVal);
        redisLock.unLock(key, v);
        log.info("[场景4结果] 续期正常");
    }

    // ============================================================
    // 场景5: 错误value无法解锁
    // ============================================================

    private void scenario5WrongValueUnlock() {
        log.info("┌──────────────────────────────────────────────┐");
        log.info("│  场景5: 错误value无法解锁                        │");
        log.info("└──────────────────────────────────────────────┘");

        final String key = PREFIX + "wrong";
        assertTrue(redisLock.lock(key, "owner-A", 30_000L));
        redisLock.unLock(key, "intruder-B");
        String cur = stringRedisTemplate.opsForValue().get(key);
        assertEquals("owner-A", cur, "错误value不应解锁");

        Object entry = stringRedisTemplate.opsForHash().get(RedisLockManagerThread.globalLockTable, key);
        assertNotNull(entry, "globalLockTable中应仍有记录");

        redisLock.unLock(key, "owner-A");
        assertNull(stringRedisTemplate.opsForValue().get(key));
        log.info("[场景5结果] 防误删有效");
    }

    // ============================================================
    // 场景6: 锁超时清理 — 加锁不解锁, 管理线程清理
    // ============================================================

    private void scenario6TimeoutCleanup() throws Exception {
        log.info("┌──────────────────────────────────────────────┐");
        log.info("│  场景6: 锁超时清理 — 不解锁等管理线程清理(10s超时)  │");
        log.info("└──────────────────────────────────────────────┘");

        final String key = PREFIX + "timeout";
        // TTL=2s < 续期间隔10s, 确保续期线程不会续期此锁, 全局锁表时间戳不会被更新
        assertTrue(redisLock.lock(key, "timeout-v", 2_000L));
        assertNotNull(stringRedisTemplate.opsForHash().get(RedisLockManagerThread.globalLockTable, key));
        log.info("[场景6] 加锁成功 (TTL=2s), 故意不解锁, 续期线程不会续期此锁");

        boolean cleaned = false;
        for (int i = 1; i <= 18; i++) {
            Thread.sleep(1_000);
            String val = stringRedisTemplate.opsForValue().get(key);
            Object entry = stringRedisTemplate.opsForHash().get(RedisLockManagerThread.globalLockTable, key);
            if (val == null) {
                log.info("[场景6] 第{}s: 管理线程已清理, key已删除", i);
                cleaned = true;
                break;
            }
            if (i % 4 == 0) log.info("[场景6] 第{}s: key存在={}, globalLockTable存在={}",
                    i, val != null, entry != null);
        }

        assertTrue(cleaned, "管理线程应在18s内清理");
        assertTrue(redisLock.lock(key, "new-owner", 5_000L), "清理后应能重新加锁");
        redisLock.unLock(key, "new-owner");
        log.info("[场景6结果] 超时清理正常");
    }

    // ============================================================
    // 场景7: 混合压力 — 竞争+快速+长持锁同时运行
    // ============================================================

    private void scenario7MixedPressure() throws Exception {
        log.info("┌──────────────────────────────────────────────┐");
        log.info("│  场景7: 混合压力 — 竞争+快速+长持锁同时运行        │");
        log.info("└──────────────────────────────────────────────┘");

        ExecutorService pool = Executors.newFixedThreadPool(40);
        CountDownLatch gate = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(40);
        AtomicInteger inSection = new AtomicInteger(0);
        AtomicInteger violations = new AtomicInteger(0);
        LongAdder ok = new LongAdder();
        LongAdder fail = new LongAdder();

        // 组1: 10线程竞争同一热点key
        String hotKey = PREFIX + "hot";
        for (int i = 0; i < 10; i++) {
            final int id = i;
            pool.submit(() -> {
                try { gate.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
                String v = "hot-" + id;
                for (int r = 0; r < 20; r++) {
                    if (redisLock.lock(hotKey, v, 30_000L)) {
                        ok.increment();
                        int n = inSection.incrementAndGet();
                        if (n > 1) violations.incrementAndGet();
                        try { Thread.sleep(ThreadLocalRandom.current().nextInt(1, 11)); }
                        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                        inSection.decrementAndGet();
                        redisLock.unLock(hotKey, v);
                    } else {
                        fail.increment();
                    }
                }
                done.countDown();
            });
        }

        // 组2: 20线程各自快速加解锁
        for (int i = 0; i < 20; i++) {
            final int id = i;
            pool.submit(() -> {
                try { gate.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
                String key = PREFIX + "fast-" + id;
                String v = "fast-" + id;
                for (int r = 0; r < 50; r++) {
                    if (redisLock.lock(key, v, 30_000L)) { ok.increment(); redisLock.unLock(key, v); }
                    else { fail.increment(); }
                }
                done.countDown();
            });
        }

        // 组3: 10线程持锁50-200ms
        for (int i = 0; i < 10; i++) {
            final int id = i;
            pool.submit(() -> {
                try { gate.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
                String key = PREFIX + "long-" + id;
                String v = "long-" + id;
                for (int r = 0; r < 5; r++) {
                    if (redisLock.lock(key, v, 30_000L)) {
                        ok.increment();
                        try { Thread.sleep(ThreadLocalRandom.current().nextInt(50, 200)); }
                        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                        redisLock.unLock(key, v);
                    } else {
                        fail.increment();
                    }
                }
                done.countDown();
            });
        }

        long t0 = System.currentTimeMillis();
        gate.countDown();
        done.await(180, TimeUnit.SECONDS);
        long elapsed = System.currentTimeMillis() - t0;
        pool.shutdown();

        int s0 = renewKeysBucket.getBucket(0).size();
        int s1 = renewKeysBucket.getBucket(1).size();
        log.info("[场景7结果] 操作={}, 成功={}, 失败={}, 违规={}, 耗时={}ms",
                ok.sum() + fail.sum(), ok.sum(), fail.sum(), violations.get(), elapsed);
        log.info("[场景7结果] renewKeysBucket: shard0={}, shard1={}", s0, s1);
        assertEquals(0, violations.get(), "场景7互斥违规");
        assertTrue(ok.sum() > 0);
    }

    // ============================================================
    // 场景8: 并发解锁竞态
    // ============================================================

    private void scenario8ConcurrentUnlockRace() throws Exception {
        log.info("┌──────────────────────────────────────────────┐");
        log.info("│  场景8: 并发解锁竞态 — 20线程同时尝试解锁         │");
        log.info("└──────────────────────────────────────────────┘");

        final String key = PREFIX + "unlock-race";
        assertTrue(redisLock.lock(key, "owner", 30_000L));
        CountDownLatch done = new CountDownLatch(20);

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                redisLock.unLock(key, "impostor");
                done.countDown();
            }).start();
        }
        done.await(10, TimeUnit.SECONDS);

        assertEquals("owner", stringRedisTemplate.opsForValue().get(key), "非持有者不能解锁");
        redisLock.unLock(key, "owner");
        assertNull(stringRedisTemplate.opsForValue().get(key));
        log.info("[场景8结果] 竞态解锁防护正常");
    }

    // ============================================================
    // 场景9: renewKeysBucket 内存残留检查
    // ============================================================

    private void scenario9BucketMemoryCheck() throws Exception {
        log.info("┌──────────────────────────────────────────────┐");
        log.info("│  场景9: renewKeysBucket 内存残留检查              │");
        log.info("└──────────────────────────────────────────────┘");

        final int rounds = 200;
        final int distinctKeys = 50;

        for (int r = 0; r < rounds; r++) {
            String key = PREFIX + "mem-" + (r % distinctKeys);
            String v = "v-" + r;
            if (redisLock.lock(key, v, 30_000L)) {
                redisLock.unLock(key, v);
            }
        }

        Thread.sleep(500);

        int s0 = 0, s1 = 0;
        for (int i = 0; i < renewKeysBucket.shardCount(); i++) {
            int cnt = 0;
            for (String k : renewKeysBucket.getBucket(i).keySet()) {
                if (k.startsWith(PREFIX)) cnt++;
            }
            if (i == 0) s0 = cnt; else s1 = cnt;
        }

        Set<Object> hashKeys = stringRedisTemplate.opsForHash().keys(RedisLockManagerThread.globalLockTable);
        int tableResidual = 0;
        if (hashKeys != null) {
            for (Object hk : hashKeys) {
                if (hk.toString().startsWith(PREFIX)) tableResidual++;
            }
        }

        log.info("[场景9结果] renewKeysBucket残留: shard0={}, shard1={}, globalLockTable残留={}",
                s0, s1, tableResidual);
        assertEquals(0, s0, "shard0不应残留测试key");
        assertEquals(0, s1, "shard1不应残留测试key");
        assertEquals(0, tableResidual, "globalLockTable不应残留测试key");
        log.info("[场景9结果] 无内存残留");
    }
}
