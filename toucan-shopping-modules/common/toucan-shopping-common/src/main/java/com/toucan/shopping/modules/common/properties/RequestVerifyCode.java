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
     * 请求验证失败页面
     */
    private String verifyFaildPage;

    /**
     * 拦截指定路径
     */
    private String pathPatterns;

    /**
     * 忽略拦截的后缀
     */
    private String excludePathPatterns;
}
