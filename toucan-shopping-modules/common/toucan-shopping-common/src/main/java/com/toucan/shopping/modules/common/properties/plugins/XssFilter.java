package com.toucan.shopping.modules.common.properties.plugins;

import lombok.Data;

import java.util.List;

/**
 * Xss过滤器
 */
@Data
public class XssFilter {

    /**
     * 是否启用XSS过滤器
     */
    private boolean enabled = false;


    /**
     * 忽略的路径
     */
    private List<String> excludePaths;

}
