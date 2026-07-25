package com.toucan.shopping.modules.common.lock.redis.thread;

import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.lock.redis.bucket.RenewKeysBucket;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.Set;

/**
 * 全局锁管理线程,定期扫描globalLockTable,强制释放超时未释放的锁
 */
public class RedisLockManagerThread extends Thread {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 全局锁表,记录所有锁的key以及创建时间
     */
    public static String globalLockTable = "global_lock_table";

    /**
     * 全局锁管理线程 每隔多少时间管理一次
     */
    public static long redisManagerExecMillisecond = 4000;

    /**
     * 锁过期时间,默认2分钟超时
     */
    public static long lockTimeOutMillisecond = 1000 * 60 * 2;

    /**
     * 开启全局锁管理线程
     */
    public static boolean enableLockManager = true;

    @Setter
    private StringRedisTemplate stringRedisTemplate;

    @Setter
    private RenewKeysBucket renewKeysBucket;

    private volatile boolean running = true;

    @Override
    public void run() {
        logger.info("锁管理线程 {} 启动,扫描间隔:{}ms,超时阈值:{}ms",
                getName(), redisManagerExecMillisecond, lockTimeOutMillisecond);
        while (running && RedisLockManagerThread.enableLockManager) {
            try {
                Set<Object> lockKeys = stringRedisTemplate.opsForHash()
                        .keys(RedisLockManagerThread.globalLockTable);
                if (!CollectionUtils.isEmpty(lockKeys)) {
                    Iterator<Object> lockKeyIterator = lockKeys.iterator();
                    while (lockKeyIterator.hasNext()) {
                        String lockKey = String.valueOf(lockKeyIterator.next());
                        try {
                            String lockCreateTime = String.valueOf(
                                    stringRedisTemplate.opsForHash()
                                            .get(RedisLockManagerThread.globalLockTable, lockKey));
                            // 如果创建时间为null或已超时,强制释放
                            if ("null".equals(lockCreateTime) || StringUtils.isEmpty(lockCreateTime)
                                    || DateUtils.currentDate().getTime() - Long.parseLong(lockCreateTime) >= RedisLockManagerThread.lockTimeOutMillisecond) {
                                logger.info("删除超时锁 {},创建时间:{}", lockKey, lockCreateTime);
                                // 从续期分片桶中移除,续期线程不再续期此锁
                                renewKeysBucket.remove(lockKey);
                                // 删除Redis中的锁key
                                stringRedisTemplate.opsForValue().getOperations().delete(lockKey);
                                // 从锁表中删除记录
                                stringRedisTemplate.opsForHash()
                                        .delete(RedisLockManagerThread.globalLockTable, lockKey);
                            }
                        } catch (Exception e) {
                            logger.warn("处理锁key:{} 异常: {}", lockKey, e.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn("锁管理线程 {} 异常: {}", getName(), e.getMessage(), e);
            }finally{
                try {
                    Thread.sleep(RedisLockManagerThread.redisManagerExecMillisecond);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();  // 恢复中断标志
                    break;                                // 退出循环
                }
            }
        }
        logger.info("锁管理线程 {} 已退出", getName());
    }

    public void shutdown() {
        this.running = false;
        this.interrupt();
    }
}
