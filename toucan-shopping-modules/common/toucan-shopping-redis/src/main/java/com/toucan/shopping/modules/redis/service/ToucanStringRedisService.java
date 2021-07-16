package com.toucan.shopping.modules.redis.service;


import java.util.concurrent.TimeUnit;

public interface ToucanStringRedisService {

    Object get(String key);

    void set(String key,Object val);

    Boolean expire(String key, long timeout, TimeUnit unit);

    boolean delete(String key);
}

