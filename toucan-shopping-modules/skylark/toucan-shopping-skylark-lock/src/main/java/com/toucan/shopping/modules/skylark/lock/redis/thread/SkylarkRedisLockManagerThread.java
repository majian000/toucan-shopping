package com.toucan.shopping.modules.skylark.lock.redis.thread;

import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.skylark.lock.redis.SkylarkRedisLock;
import com.toucan.shopping.modules.skylark.lock.redis.impl.SkylarkRedisLockImpl;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.Set;

/**
 * 管理那些持有了很久没有释放的锁,将有这个类释放
 */
@Data
public class SkylarkRedisLockManagerThread extends Thread {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 全局锁表,记录所有锁的key以及创建时间
     */
    public static String globalLockTable ="global_lock_table";


    /**
     * 全局锁管理线程 每隔多少时间管理一次
     */
    public static long redisManagerExecMillisecond = 4000;

    /**
     * 锁过期时间,默认2分钟超时
     */
    public static long lockTimeOutMillisecond = 1000*60*2;

    /**
     * 开启全局锁管理线程
     */
    public static boolean enableLockManager =true;

    private RedisTemplate redisTemplate;

    private SkylarkRedisLock redisLock;

    @Override
    public void run() {
        while(SkylarkRedisLockManagerThread.enableLockManager) {
            try {
//                logger.info("锁管理线程启动 查询表:"+ SkylarkRedisLockManagerThread.globalLockTable);
                Set<Object> lockKeys = redisTemplate.opsForHash().keys(SkylarkRedisLockManagerThread.globalLockTable);
                if(!CollectionUtils.isEmpty(lockKeys)) {
                    Iterator lockKeyIterator = lockKeys.iterator();
                    while(lockKeyIterator.hasNext()) {
                        //拿到锁的创建时间
                        String lockKey = String.valueOf(lockKeyIterator.next());
                        //如果对象为空 lockCreateTime的值将为"null"
                        String lockCreateTime =  String.valueOf(redisTemplate.opsForHash().get(SkylarkRedisLockManagerThread.globalLockTable, lockKey));
                        //如果这个锁已经很久没释放,将强制释放这个锁
                        if("null".equals(lockCreateTime)||StringUtils.isEmpty(lockCreateTime)||DateUtils.currentDate().getTime()-Long.parseLong(lockCreateTime)>= SkylarkRedisLockManagerThread.lockTimeOutMillisecond)
                        {
                            logger.info("删除超时锁 {} 创建时间 {}",lockKey,lockCreateTime);
                            if(((SkylarkRedisLockImpl)redisLock).getThreadHashMap().get(lockKey)!=null)
                            {
                                ((SkylarkRedisLockImpl)redisLock).getThreadHashMap().get(lockKey).setLoop(false);
                                ((SkylarkRedisLockImpl)redisLock).getThreadHashMap().remove(lockKey);
                            }
                            redisTemplate.opsForValue().getOperations().delete(lockKey);

                            //从锁表中删除这个锁
                            redisTemplate.opsForHash().delete(SkylarkRedisLockManagerThread.globalLockTable,((Object)lockKey));
                        }
                    }
                }
                //锁管理线程休眠
                this.sleep(SkylarkRedisLockManagerThread.redisManagerExecMillisecond);
            } catch (Exception e) {
                logger.warn(e.getMessage(),e);
            }
        }
    }
}
