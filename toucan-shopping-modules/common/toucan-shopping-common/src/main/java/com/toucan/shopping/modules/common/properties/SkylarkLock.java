package com.toucan.shopping.modules.common.properties;


import lombok.Data;

/**
 * 云雀 分布式锁
 */
@Data
public class SkylarkLock {

    /**
     * redis 配置
     */
    private Redis redis;
}
