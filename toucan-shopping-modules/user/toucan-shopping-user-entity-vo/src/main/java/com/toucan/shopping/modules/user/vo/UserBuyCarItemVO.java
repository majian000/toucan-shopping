package com.toucan.shopping.modules.user.vo;

import com.toucan.shopping.modules.user.entity.UserBuyCarItem;
import lombok.Data;


/**
 * 购物车
 * @author majian
 */
@Data
public class UserBuyCarItemVO extends UserBuyCarItem {


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
