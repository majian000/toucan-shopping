package com.toucan.shopping.modules.common.properties.modules.contentCache;


import lombok.Data;

/**
 * 内容服务缓存
 */
@Data
public class ContentCache {

    /**
     * redis 配置
     */
    private ContentCacheRedis redis;
}
