package com.toucan.shopping.modules.area.cache.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.area.cache.service.AreaRedisService;
import com.toucan.shopping.modules.area.constant.AreaRedisKey;
import com.toucan.shopping.modules.area.vo.AreaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AreaRedisServiceImpl implements AreaRedisService {

    @Autowired
    @Qualifier("areaCacheRedisTemplate")
    private RedisTemplate redisTemplate;

    @Override
    public void flushProvinceCache(List<AreaVO> areaVOS) {
        redisTemplate.opsForValue().set(AreaRedisKey.getProvinceCacheKey(), JSONObject.toJSONString(areaVOS));
    }

    @Override
    public void flushCityCache(String key,List<AreaVO> areaVOS) {
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(areaVOS));
    }

    @Override
    public void flushAreaCache(String key,List<AreaVO> areaVOS) {
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(areaVOS));
    }

    @Override
    public void clearAreaCache() {
        redisTemplate.delete(redisTemplate.keys(AreaRedisKey.getCachePrefixKey()+":*"));
    }

}
