package com.toucan.shopping.modules.common.properties.modules.adminAuthCache;


import com.toucan.shopping.modules.common.properties.modules.areaCache.AreaCacheRedis;
import lombok.Data;

/**
 * 权限服务缓存
 */
@Data
public class AdminAuthCache {

    /**
     * redis 配置
     */
    private AdminAuthCacheRedis redis;
}
