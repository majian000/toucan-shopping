package com.toucan.shopping.modules.user.login.cache.service;

import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.user.vo.UserVO;

import java.util.Map;

/**
 * 用户登录缓存
 */
public interface UserLoginCacheService {


    ToucanStringRedisService routeToucanRedisService(UserVO userVO);


    ToucanStringRedisService routeToucanRedisServiceByUserMainId(Long userMainId);

}
