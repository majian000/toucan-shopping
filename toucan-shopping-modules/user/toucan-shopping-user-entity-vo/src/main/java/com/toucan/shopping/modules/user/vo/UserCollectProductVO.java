package com.toucan.shopping.modules.user.vo;

import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.entity.UserCollectProduct;
import lombok.Data;

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



}
