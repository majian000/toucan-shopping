package com.toucan.shopping.modules.redis.service;


import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface ToucanStringRedisService {

    Object get(String key);

    void set(String key,Object val);

    Boolean expire(String key, long timeout, TimeUnit unit);

    boolean delete(String key);

    Set<String> keys(String pattern);

    void put(String var1, String var2, String var3);

    Object get(String var1, Object var2);

    Long delete(String var1, Object... var2);

    RedisTemplate getRedisTemplate();

}

