package com.toucan.shopping.modules.common.properties;

import lombok.Data;

/**
 * 模块配置
 */
@Data
public class Modules {

    /**
     * 地区缓存模块
     */
    private AreaCache areaCache;

    /**
     * 类别缓存模块
     */
    private CategoryCache categoryCache;

    /**
     * 云雀 分布式锁
     */
    private SkylarkLock skylarkLock;

    /**
     * redis封装
     */
    private ToucanRedis toucanRedis;
}
