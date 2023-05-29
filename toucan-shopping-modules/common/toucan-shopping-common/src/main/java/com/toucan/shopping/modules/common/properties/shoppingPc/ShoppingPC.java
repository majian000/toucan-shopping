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
     *
     * 根路径
     */
    private String basePath;


    /**
     * 用户注册页
     */
    private String registPage;


    /**
     * 商品审核预览页
     */
    private String productApprovePreviewPage;

    /**
     * 商品详情页
     */
    private String productDetailPage;

    /**
     * 商品详情页
     */
    private String productPreviewPage;

    /**
     * 商品详情页
     */
    private String productSkuPreviewPage;
    /**
     * 商品详情页
     */
    private String productSkuDetailPage;
    /**
     * 首页商品栏目编码
     */
    private String pcIndexColumnTypeCode;

    /**
     * 应用编码
     */
    private String appCode;

}
