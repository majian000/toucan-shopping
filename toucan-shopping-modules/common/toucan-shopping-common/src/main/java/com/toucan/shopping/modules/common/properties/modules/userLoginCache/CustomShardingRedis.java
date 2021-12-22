package com.toucan.shopping.modules.common.properties.modules.userLoginCache;

import lombok.Data;

import java.util.List;

/**
 * 用户自定义分片
 */
@Data
public class CustomShardingRedis {


    /**
     * 登录redis缓存
     */
    private List<UserLoginRedis> loginCacheRedisList;



}
