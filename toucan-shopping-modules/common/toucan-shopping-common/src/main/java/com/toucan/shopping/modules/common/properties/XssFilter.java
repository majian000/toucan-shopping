package com.toucan.shopping.modules.common.properties;

import lombok.Data;

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
    private String excludePaths;

}
