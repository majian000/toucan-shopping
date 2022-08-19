package com.toucan.shopping.modules.user.vo;

import com.toucan.shopping.modules.user.entity.UserBuyCar;
import lombok.Data;


/**
 * 购物车
 * @author majian
 */
@Data
public class UserBuyCarVO extends UserBuyCar {


    /**
     * 商品SKU名称
     */
    private String productSkuName;

    /**
     * 商品SKU预览图
     */
    private String httpProductImgPath;

    /**
     * 商品价格
     */
    private Double productPrice;

    /**
     * 属性预览
     */
    private String attributePreview;

}
