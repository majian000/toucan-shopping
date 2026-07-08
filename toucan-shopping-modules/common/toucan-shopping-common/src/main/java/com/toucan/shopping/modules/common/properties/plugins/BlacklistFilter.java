package com.toucan.shopping.modules.common.properties.plugins;

import lombok.Data;

import java.util.List;

/**
 * 黑名单过滤器
 */
@Data
public class BlacklistFilter {

    /**
     * 是否启用黑名单过滤器
     */
    private boolean enabled = false;


}
