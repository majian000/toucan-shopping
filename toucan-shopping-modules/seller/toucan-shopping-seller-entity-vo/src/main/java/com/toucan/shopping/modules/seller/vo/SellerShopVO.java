package com.toucan.shopping.modules.seller.vo;

import com.toucan.shopping.modules.seller.entity.SellerShop;
import lombok.Data;

/**
 * 商户店铺信息
 */
@Data
public class SellerShopVO extends SellerShop {

    /**
     * 验证码
     */
    private String vcode;

}
