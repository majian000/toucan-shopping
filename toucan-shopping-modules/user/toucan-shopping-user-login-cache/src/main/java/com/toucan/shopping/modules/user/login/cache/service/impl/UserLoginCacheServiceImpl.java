package com.toucan.shopping.modules.user.login.cache.service.impl;

import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.user.login.cache.service.UserLoginCacheService;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

public class UserLoginCacheServiceImpl implements UserLoginCacheService {

    private Map<String, ToucanStringRedisService> toucanStringRedisServiceMap = new HashMap<String, ToucanStringRedisService>();


    @Override
    public void login(UserVO userVO) {
        if(userVO.getUserMainId()==null)
        {
            throw new IllegalArgumentException("用户主ID不能为空");
        }

        ToucanStringRedisService toucanStringRedisService = toucanStringRedisServiceMap.get(routeIndex(userVO));
        RedisTemplate redisTemplate = toucanStringRedisService.getRedisTemplate();
        LettuceConnectionFactory connectionFactory = (LettuceConnectionFactory) redisTemplate.getConnectionFactory();
    }


    public String routeIndex(UserVO userVO)
    {
        return String.valueOf(userVO.getUserMainId()%toucanStringRedisServiceMap.size());
    }

    @Override
    public Map<String, ToucanStringRedisService> getToucanStringRedisServiceMap() {
        return toucanStringRedisServiceMap;
    }
}
