package com.toucan.shopping.modules.common.properties.plugins;

import lombok.Data;

/**
 * 链路追踪配置
 */
@Data
public class TraceId {

    /**
     * 是否启用
     */
    private boolean enabled = false;
}
