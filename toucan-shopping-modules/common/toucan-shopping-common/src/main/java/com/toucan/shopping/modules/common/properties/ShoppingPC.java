package com.toucan.shopping.modules.common.properties;

import lombok.Data;

/**
 * PC端配置
 */
@Data
public class ShoppingPC {

    /**
     * PC端服务地址
     */
    private String ipList;

    /**
     * freemarker自定义配置
     */
    private Freemarker freemarker;

    /**
     * 用户实名审核页面
     */
    private String realNameAuthenticationPage;
}
