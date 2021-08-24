package com.toucan.shopping.modules.seller.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.entity.SellerShopLoginHistory;
import com.toucan.shopping.modules.seller.page.SellerShopPageInfo;

import java.util.List;

/**
 * 卖家店铺登录历史服务
 * @author majian
 * @date 2021-8-24 16:11:11
 */
public interface SellerShopLoginHistoryService {


    int save(SellerShopLoginHistory entity);


}
