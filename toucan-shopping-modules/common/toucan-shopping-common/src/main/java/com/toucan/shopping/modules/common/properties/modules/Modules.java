package com.toucan.shopping.modules.common.properties.modules;

import com.toucan.shopping.modules.common.properties.modules.areaCache.AreaCache;
import com.toucan.shopping.modules.common.properties.modules.categoryCache.CategoryCache;
import com.toucan.shopping.modules.common.properties.modules.log.Log;
import com.toucan.shopping.modules.common.properties.modules.skylarkLock.SkylarkLock;
import com.toucan.shopping.modules.common.properties.modules.toucanRedis.ToucanRedis;
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

    /**
     * 日志
     */
    private Log log;
}
