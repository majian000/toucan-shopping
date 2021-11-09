package com.toucan.shopping.modules.user.login.cache.service;

import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.user.vo.UserVO;

import java.util.Map;

/**
 * 用户登录缓存
 */
public interface UserLoginCacheService {



    /**
     * 返回索引和redis对象的键值对
     * @return
     */
    Map<String, Map<String,ToucanStringRedisService>> getToucanStringRedisServiceMap();

    /**
     * 索引和db数量的键值对
     * @return
     */
    Map<String,Integer> getDbCountMap();

    ToucanStringRedisService routeToucanRedisService(UserVO userVO);


    ToucanStringRedisService routeToucanRedisServiceByUserMainId(Long userMainId);

}
