package com.toucan.shopping.modules.common.properties.shoppingPc;

import com.toucan.shopping.modules.common.properties.shoppingPc.freemarker.Freemarker;
import lombok.Data;

/**
 * 卖家PC端配置
 */
@Data
public class ShoppingSellerWebPC {

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

}
