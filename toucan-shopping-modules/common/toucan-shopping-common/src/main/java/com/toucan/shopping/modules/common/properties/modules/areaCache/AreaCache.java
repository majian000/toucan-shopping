package com.toucan.shopping.modules.common.properties.modules.areaCache;


import lombok.Data;

/**
 * 地区服务缓存
 */
@Data
public class AreaCache {

    /**
     * redis 配置
     */
    private AreaCacheRedis redis;
}
