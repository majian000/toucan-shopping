package com.toucan.shopping.modules.common.properties.modules;

import com.toucan.shopping.modules.common.properties.modules.adminAuthCache.AdminAuthCache;
import com.toucan.shopping.modules.common.properties.modules.areaCache.AreaCache;
import com.toucan.shopping.modules.common.properties.modules.categoryCache.CategoryCache;
import com.toucan.shopping.modules.common.properties.modules.contentCache.ContentCache;
import com.toucan.shopping.modules.common.properties.modules.log.Log;
import com.toucan.shopping.modules.common.properties.modules.skylarkLock.SkylarkLock;
import com.toucan.shopping.modules.common.properties.modules.toucanRedis.ToucanRedis;
import com.toucan.shopping.modules.common.properties.modules.userLoginCache.UserLoginCache;
import lombok.Data;

/**
 * 模块配置
 */
@Data
public class Modules {


    /**
     * 权限缓存模块
     */
    private AdminAuthCache adminAuthCache;

    /**
     * 地区缓存模块
     */
    private AreaCache areaCache;

    /**
     * 内容缓存模块
     */
    private ContentCache contentCache;

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
     * 用户登录缓存
     */
    private UserLoginCache userLoginCache;

    /**
     * 日志
     */
    private Log log;
}
