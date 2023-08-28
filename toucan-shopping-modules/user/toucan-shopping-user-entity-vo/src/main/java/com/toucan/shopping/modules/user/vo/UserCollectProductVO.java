package com.toucan.shopping.modules.user.vo;

import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.entity.UserCollectProduct;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 收藏商品
 */
@Data
public class UserCollectProductVO extends UserCollectProduct {

    /**
     * 商品SKU ID列表
     */
    private List<Long> productSkuIds;


    /**
     * 收藏类型 0:取消 1:收藏
     */
    private int type=1;

    /**
     * 商品名称
     */
    private String productSkuName;

    /**
     * 商品价格
     */
    private BigDecimal productPrice;

    /**
     * 商品SKU预览图
     */
    private String httpProductImgPath;

}
