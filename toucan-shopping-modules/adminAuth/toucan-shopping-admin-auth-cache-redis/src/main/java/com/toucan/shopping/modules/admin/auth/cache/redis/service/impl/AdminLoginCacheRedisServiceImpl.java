package com.toucan.shopping.modules.admin.auth.cache.redis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.cache.service.AdminLoginCacheService;
import com.toucan.shopping.modules.admin.auth.cache.service.AppCacheService;
import com.toucan.shopping.modules.admin.auth.redis.AdminAuthRedisKey;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("adminLoginCacheRedisService")
public class AdminLoginCacheRedisServiceImpl implements AdminLoginCacheService {

    @Autowired
    @Qualifier("adminAuthCacheRedisTemplate")
    private RedisTemplate redisTemplate;

    @Override
    public void loginToken(String adminId, String appCode, String loginToken) {
        //登录哈希表 例如 00DDEXXAA0_LOGIN_TOKENS:[{00DDEXXAA0_LOGIN_TOKENS_10000001:"XXXXXX"},{00DDEXXAA0_LOGIN_TOKENS_10000002:"YYYYYYY"}]
        redisTemplate.opsForHash().put(AdminAuthRedisKey.getLoginTokenGroupKey(adminId),
                AdminAuthRedisKey.getLoginTokenAppKey(adminId,appCode),loginToken);
        //设置登录token1个小时超时
        redisTemplate.expire(AdminAuthRedisKey.getLoginTokenGroupKey(adminId),
                AdminAuthRedisKey.LOGIN_TIMEOUT_SECOND, TimeUnit.SECONDS);
    }

    @Override
    public Object getLoginToken(String adminId, String appCode) {
        return redisTemplate.opsForHash().get(AdminAuthRedisKey.getLoginTokenGroupKey(adminId), AdminAuthRedisKey.getLoginTokenAppKey(adminId,appCode));
    }

    @Override
    public void deleteLoginToken(String adminId, String appCode) {

        //删除对应的登录会话
        redisTemplate.opsForHash().delete(AdminAuthRedisKey.getLoginTokenGroupKey(adminId)
                , AdminAuthRedisKey.getLoginTokenAppKey(adminId,appCode));

    }

    @Override
    public void loginTokenDelay(String adminId) {
        redisTemplate.expire(AdminAuthRedisKey.getLoginTokenGroupKey(adminId),
                AdminAuthRedisKey.LOGIN_TIMEOUT_SECOND, TimeUnit.SECONDS);
    }
}
