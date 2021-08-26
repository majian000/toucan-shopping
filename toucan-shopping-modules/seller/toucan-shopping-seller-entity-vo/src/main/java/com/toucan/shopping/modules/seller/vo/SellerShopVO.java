package com.toucan.shopping.modules.seller.vo;

import com.toucan.shopping.modules.seller.entity.SellerShop;
import lombok.Data;

import java.util.List;

/**
 * 商户店铺信息
 */
@Data
public class SellerShopVO extends SellerShop {

    /**
     * 验证码
     */
    private String vcode;

    /**
     * 外网访问的logo地址
     */
    private String httpLogo;


    /**
     * 用户登录历史
     */
    private List<SellerLoginHistoryVO> loginHistoryList;

    /**
     * 上一次登录时间
     */
    private String lastLoginTime;

}
