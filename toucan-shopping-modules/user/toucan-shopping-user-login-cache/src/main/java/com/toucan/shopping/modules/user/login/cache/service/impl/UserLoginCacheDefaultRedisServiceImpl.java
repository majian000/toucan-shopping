package com.toucan.shopping.modules.user.login.cache.service.impl;

import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.user.login.cache.service.UserLoginCacheService;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;


/**
 * 登录缓存 默认实现
 * @auth majian
 */
public class UserLoginCacheDefaultRedisServiceImpl implements UserLoginCacheService {

    private ToucanStringRedisService toucanStringRedisService;

    public void setToucanStringRedisService(ToucanStringRedisService toucanStringRedisService) {
        this.toucanStringRedisService = toucanStringRedisService;
    }

    public ToucanStringRedisService routeToucanRedisService(UserVO userVO)
    {
        return toucanStringRedisService;
    }

    @Override
    public ToucanStringRedisService routeToucanRedisServiceByUserMainId(Long userMainId) {
        UserVO userVO = new UserVO();
        userVO.setUserMainId(userMainId);
        return routeToucanRedisService(userVO);
    }

}
