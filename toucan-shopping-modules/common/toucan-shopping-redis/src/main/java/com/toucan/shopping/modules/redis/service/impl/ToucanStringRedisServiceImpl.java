package com.toucan.shopping.modules.redis.service.impl;

import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ToucanStringRedisServiceImpl implements ToucanStringRedisService {

    @Autowired
    @Qualifier("toucanRedisTemplate")
    private RedisTemplate redisTemplate;


    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(String key, Object val) {
        redisTemplate.opsForValue().set(key,val);
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key,timeout, unit);
    }

    @Override
    public boolean delete(String key) {
        return redisTemplate.opsForValue().getOperations().delete(key);
    }
}
