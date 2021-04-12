package com.toucan.shopping.modules.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.user.entity.User;
import com.toucan.shopping.modules.user.redis.UserCenterUserCacheRedisKey;
import com.toucan.shopping.modules.user.service.UserRedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class UserRedisServiceImpl implements UserRedisService {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public List<User> findListByMobileOrEmail(String username) {
        List<User> users = new ArrayList<User>();
        String userCache = stringRedisTemplate.opsForValue().get(UserCenterUserCacheRedisKey.getUserCacheEmailKey(username));
        if(StringUtils.isNotEmpty(userCache))
        {
            users.add(JSONObject.parseObject(userCache,User.class));
            return users;
        }

        userCache = stringRedisTemplate.opsForValue().get(UserCenterUserCacheRedisKey.getUserCacheMobilePhoneKey(username));
        if(StringUtils.isNotEmpty(userCache))
        {
            users.add(JSONObject.parseObject(userCache,User.class));
            return users;
        }

        return users;
    }
}
