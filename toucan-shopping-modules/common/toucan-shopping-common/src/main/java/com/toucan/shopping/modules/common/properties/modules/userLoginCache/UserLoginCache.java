package com.toucan.shopping.modules.common.properties.modules.userLoginCache;

import com.toucan.shopping.modules.common.properties.modules.skylarkLock.SkylarkLockRedis;
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
     * 实现方式类型 default 默认采用官方配置 customSharding 自定义分片
     */
    private String redisType;

    /**
     * 这个节点的db数量,默认16个节点
     */
    private Integer dbCount = 16;

    /**
     * 默认redis 配置
     */
    private UserLoginRedis defaultRedis;

    /**
     * 自定义分片redis 配置
     */
    private CustomShardingRedis customShardingRedis;


}
