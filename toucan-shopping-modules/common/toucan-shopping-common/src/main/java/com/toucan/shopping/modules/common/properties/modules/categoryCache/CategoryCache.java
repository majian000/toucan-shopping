package com.toucan.shopping.modules.common.properties.modules.categoryCache;


import lombok.Data;

/**
 * 类别服务缓存
 */
@Data
public class CategoryCache {

    /**
     * redis 配置
     */
    private CategoryCacheRedis redis;
}
