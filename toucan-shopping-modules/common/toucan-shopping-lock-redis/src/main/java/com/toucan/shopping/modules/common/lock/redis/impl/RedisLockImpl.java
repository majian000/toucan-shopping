package com.toucan.shopping.modules.common.lock.redis.impl;

import com.toucan.shopping.modules.common.lock.redis.thread.RedisLockManagerThread;
import com.toucan.shopping.modules.common.lock.redis.RedisLock;
import com.toucan.shopping.modules.common.util.RenewKeysBucket;
import jakarta.annotation.Resource;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Data
public class RedisLockImpl implements RedisLock {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 原子解锁Lua脚本: 比较value后删除,避免误删他人持有的锁
     * 返回 1: 删除成功
     * 返回 0: key存在但value不匹配(他人持有)
     * 返回 -1: key不存在(已过期或被强制释放)
     */
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    static {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(
                "if redis.call('EXISTS', KEYS[1]) == 0 then " +
                "    return -1 " +
                "end " +
                "if redis.call('GET', KEYS[1]) == ARGV[1] then " +
                "    return redis.call('DEL', KEYS[1]) " +
                "else " +
                "    return 0 " +
                "end");
        script.setResultType(Long.class);
        UNLOCK_SCRIPT = script;
    }

    /**
     * 续期key分片桶,key通过hash路由到不同分片,每个续期线程绑定一个分片
     */
    @Resource(name = "renewKeysBucket")
    private RenewKeysBucket renewKeysBucket;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 最大重试次数,可通过setter覆盖(默认1000)
     */
    private long maxRetryCount = 3;

    /**
     * 重试间隔(毫秒),可通过setter覆盖(默认50)
     */
    private long retrySleepMs = 50;


    public boolean lock(String lockKey, String lockValue) {
        return lock(lockKey, lockValue, RedisLock.DEFAULT_MILLISECOND);
    }


    public boolean lock(String lockKey, String lockValue, long millisecond) {

        int tryCount = 1;
        while (true) {
            if (tryCount >= maxRetryCount) {
                logger.warn("redis key " + lockKey + " 已存在 重试次数已到" + maxRetryCount);
                break;
            }
            tryCount++;
            //利用setIfAbsent原子操作同时设置key和过期时间,避免SETNX+EXPIRE两步操作的非原子性问题
            Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, millisecond, TimeUnit.MILLISECONDS);
            if (result != null && result.booleanValue()) {
                // 先将key加入本地续期分片桶(本地操作,不会失败),由对应分片的续期线程负责续期
                renewKeysBucket.put(lockKey, millisecond);
                try {
                    // 再将key记录到全局锁表(Redis操作可能失败)
                    stringRedisTemplate.opsForHash().put(RedisLockManagerThread.globalLockTable, lockKey, String.valueOf(System.currentTimeMillis()));
                    return true;
                } catch (Exception e) {
                    // Redis元数据写入失败，回滚：清理本地续期桶 + 删除已获取的Redis锁key，避免产生无法管理的孤儿锁
                    logger.warn("锁元数据写入失败,回滚释放锁 {}: {}", lockKey, e.getMessage());
                    renewKeysBucket.remove(lockKey);
                    try {
                        stringRedisTemplate.delete(lockKey);
                    } catch (Exception ignored) {
                        logger.warn("回滚删除锁key {} 失败", lockKey);
                    }
                    return false;
                }
            }
            //重试间隔,避免忙等对Redis造成压力
            try {
                Thread.sleep(retrySleepMs);
            } catch (InterruptedException e) {
                logger.warn("lock retry sleep interrupted for key " + lockKey, e);
                Thread.currentThread().interrupt();
                break;
            }
        }
        return false;
    }


    public void unLock(String lockKey, String lockValue) {
        // Lua脚本原子执行 EXISTS + GET + DEL,返回:
        //   0: value不匹配  → 锁被他人持有,不做任何清理
        //   1: 删除成功     → 清理本地桶和全局锁表
        //  -1: key不存在    → 清理本地残留状态
        Long result = stringRedisTemplate.execute(UNLOCK_SCRIPT, List.of(lockKey), lockValue);
        if (result != null && result != 0) {
            // 锁已释放(key被删除或已过期),清理本地续期桶和全局锁表中的残留数据
            renewKeysBucket.remove(lockKey);
            stringRedisTemplate.opsForHash().delete(RedisLockManagerThread.globalLockTable, lockKey);
        }
    }


}
