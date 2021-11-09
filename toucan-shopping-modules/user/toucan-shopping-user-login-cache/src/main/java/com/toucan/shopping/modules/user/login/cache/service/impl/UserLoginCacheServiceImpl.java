package com.toucan.shopping.modules.user.login.cache.service.impl;

import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.user.login.cache.service.UserLoginCacheService;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

public class UserLoginCacheServiceImpl implements UserLoginCacheService {

    private Map<String, Map<String,ToucanStringRedisService>> toucanStringRedisServiceMap = new HashMap<String, Map<String,ToucanStringRedisService>>();
    //索引和db数量的键值对
    private Map<String,Integer> dbCountMap = new HashMap<String,Integer>();


    public ToucanStringRedisService routeToucanRedisService(UserVO userVO)
    {
        String index = routeIndex(userVO);
        Map<String,ToucanStringRedisService> toucanStringRedisServiceDBMap = toucanStringRedisServiceMap.get(index);
        return toucanStringRedisServiceDBMap.get(String.valueOf(routeDBIndex(userVO,index)));
    }

    @Override
    public ToucanStringRedisService routeToucanRedisServiceByUserMainId(Long userMainId) {
        UserVO userVO = new UserVO();
        userVO.setUserMainId(userMainId);
        return routeToucanRedisService(userVO);
    }


    public String routeIndex(UserVO userVO)
    {
        return String.valueOf((userVO.getUserMainId().hashCode()&Integer.MAX_VALUE)%toucanStringRedisServiceMap.size());
    }


    public Integer routeDBIndex(UserVO userVO,String index)
    {
        Integer dbCount = dbCountMap.get(index);
        Integer dbIndex = (userVO.getUserMainId().hashCode()&Integer.MAX_VALUE)%dbCount;
        return dbIndex;
    }

    @Override
    public Map<String, Map<String,ToucanStringRedisService>> getToucanStringRedisServiceMap() {
        return toucanStringRedisServiceMap;
    }

    @Override
    public Map<String, Integer> getDbCountMap() {
        return dbCountMap;
    }
}
