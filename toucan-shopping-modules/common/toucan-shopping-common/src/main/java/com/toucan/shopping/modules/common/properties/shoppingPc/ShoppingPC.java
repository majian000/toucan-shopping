package com.toucan.shopping.modules.common.properties.shoppingPc;

import com.toucan.shopping.modules.common.properties.shoppingPc.freemarker.Freemarker;
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
     * 用户注册页
     */
    private String registPage;

    /**
     *
     * 根路径
     */
    private String basePath;

}
