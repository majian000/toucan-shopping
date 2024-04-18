package com.toucan.shopping.cloud.apps.seller.web.service;

import com.toucan.shopping.modules.seller.vo.SellerShopVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 店铺服务类
 */
public interface ShopService {

    SellerShopVO queryByShop(String userMainId) throws Exception;


}
