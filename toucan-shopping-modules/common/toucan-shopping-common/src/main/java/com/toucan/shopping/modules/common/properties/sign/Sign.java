package com.toucan.shopping.modules.common.properties.sign;

import lombok.Data;

/**
 * 签名配置
 */
@Data
public class Sign {


    /**
     * 是否开始签名
     */
    private boolean enabled = false;

    /**
     * 签名请求头
     */
    private String signHeader = "toucan-sign-header";


    /**
     * 拦截指定路径
     */
    private String pathPatterns;


}
