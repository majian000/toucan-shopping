package com.toucan.shopping.modules.admin.auth.cache.redis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.cache.service.AdminAppCacheService;
import com.toucan.shopping.modules.admin.auth.cache.service.AppCacheService;
import com.toucan.shopping.modules.admin.auth.redis.AdminAuthRedisKey;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service("adminAppCacheRedisService")
public class AdminAppCacheRedisServiceImpl implements AdminAppCacheService {

    @Autowired
    @Qualifier("adminAuthCacheRedisTemplate")
    private RedisTemplate redisTemplate;

    @Override
    public void save(AdminAppVO adminAppVO) throws Exception {
        redisTemplate.opsForValue().set(AdminAuthRedisKey.getAdminAppRedisKey(adminAppVO.getAdminId(),adminAppVO.getAppCode()), JSONObject.toJSONString(adminAppVO));
    }

    @Override
    public boolean deleteByAdminIdAndAppCode(String adminId, String appCode) throws Exception {
        return redisTemplate.delete(AdminAuthRedisKey.getAdminAppRedisKey(adminId,appCode));
    }

    @Override
    public boolean deleteByAdminId(String adminId) {
        redisTemplate.delete(redisTemplate.keys(AdminAuthRedisKey.getAdminAppRedisKey(adminId)+":*"));
        return true;
    }

    @Override
    public AdminAppVO findByAdminIdAndAppCode(String adminId, String appCode) throws Exception {
        Object areasRedisObject = redisTemplate.opsForValue().get(AdminAuthRedisKey.getAdminAppRedisKey(adminId,appCode));
        if(areasRedisObject!=null) {
            return JSONObject.parseObject(String.valueOf(areasRedisObject), AdminAppVO.class);
        }
        return null;
    }


}
