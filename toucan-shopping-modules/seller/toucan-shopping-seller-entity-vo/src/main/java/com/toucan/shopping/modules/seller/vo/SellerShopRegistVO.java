package com.toucan.shopping.modules.seller.vo;

import com.toucan.shopping.modules.seller.entity.SellerShop;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 商户注册店铺信息
 */
@Data
public class SellerShopRegistVO extends SellerShopVO {

    /**
     * 手机号
     */
    private String mobilePhone;

}
