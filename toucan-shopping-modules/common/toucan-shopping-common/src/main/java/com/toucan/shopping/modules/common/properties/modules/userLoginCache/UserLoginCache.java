package com.toucan.shopping.modules.common.properties.modules.userLoginCache;

import lombok.Data;

import java.util.List;

/**
 * 用户登录缓存
 */
@Data
public class UserLoginCache {

    /**
     * 缓存实现类型,可选值 redis
     */
    private String cacheType;

    /**
     * 登录redis缓存
     */
    private List<UserLoginRedis> loginCacheRedisList;




}
