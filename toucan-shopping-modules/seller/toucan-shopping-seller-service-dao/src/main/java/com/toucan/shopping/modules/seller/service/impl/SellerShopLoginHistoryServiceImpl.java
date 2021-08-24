package com.toucan.shopping.modules.seller.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.entity.SellerShopLoginHistory;
import com.toucan.shopping.modules.seller.mapper.SellerShopLoginHistoryMapper;
import com.toucan.shopping.modules.seller.mapper.SellerShopMapper;
import com.toucan.shopping.modules.seller.page.SellerShopPageInfo;
import com.toucan.shopping.modules.seller.service.SellerShopLoginHistoryService;
import com.toucan.shopping.modules.seller.service.SellerShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 卖家店铺服务
 * @author majian
 * @date 2021-8-4 17:44:57
 */
@Service
public class SellerShopLoginHistoryServiceImpl implements SellerShopLoginHistoryService {


    @Autowired
    private SellerShopLoginHistoryMapper sellerShopLoginHistoryMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public int save(SellerShopLoginHistory entity) {
        return sellerShopLoginHistoryMapper.insert(entity);
    }

}
