package com.toucan.shopping.modules.admin.auth.cache.redis.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.cache.service.AppCacheService;
import com.toucan.shopping.modules.admin.auth.redis.AdminAuthRedisKey;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service("appCacheRedisService")
public class AppCacheRedisServiceImpl implements AppCacheService {

    @Autowired
    @Qualifier("adminAuthCacheRedisTemplate")
    private RedisTemplate redisTemplate;

    @Override
    public void save(AppVO appVO) throws Exception {
        redisTemplate.opsForValue().set(AdminAuthRedisKey.getAppRedisKey(appVO.getCode()), JSONObject.toJSONString(appVO));
    }

    @Override
    public boolean deleteByAppCode(String appCode) throws Exception {
        return redisTemplate.delete(AdminAuthRedisKey.getAppRedisKey(appCode));
    }

    @Override
    public AppVO findByAppCode(String appCode) throws Exception {
        Object areasRedisObject = redisTemplate.opsForValue().get(AdminAuthRedisKey.getAppRedisKey(appCode));
        if(areasRedisObject!=null) {
            return JSONObject.parseObject(String.valueOf(areasRedisObject), AppVO.class);
        }
        return null;
    }
}
