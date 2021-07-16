package com.toucan.shopping.modules.common.properties;

import lombok.Data;

/**
 * 自定义配置
 */
@Data
public class RequestVerifyCode {


    /**
     * 是否开启
     */
    private boolean enabled = false;


    /**
     * 拦截指定路径
     */
    private String pathPatterns;

    /**
     * 忽略拦截的后缀
     */
    private String excludePathPatterns;
}
