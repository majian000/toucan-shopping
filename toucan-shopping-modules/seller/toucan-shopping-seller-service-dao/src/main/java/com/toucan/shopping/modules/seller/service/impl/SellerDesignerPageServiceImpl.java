package com.toucan.shopping.modules.seller.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.SellerDesignerPage;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.mapper.SellerDesignerPageMapper;
import com.toucan.shopping.modules.seller.mapper.SellerShopMapper;
import com.toucan.shopping.modules.seller.page.SellerShopPageInfo;
import com.toucan.shopping.modules.seller.service.SellerDesignerPageService;
import com.toucan.shopping.modules.seller.service.SellerShopService;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 卖家设计器页面
 * @author majian
 * @date 2023-10-25 16:35:56
 */
@Service
public class SellerDesignerPageServiceImpl implements SellerDesignerPageService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SellerDesignerPageMapper sellerDesignerPageMapper;

    @Override
    public int save(SellerDesignerPage sellerDesignerPage) {
        return sellerDesignerPageMapper.insert(sellerDesignerPage);
    }
    @Override
    public int update(SellerDesignerPage sellerDesignerPage) {
        return sellerDesignerPageMapper.update(sellerDesignerPage);
    }


    @Override
    public SellerDesignerPage queryLastOne(SellerDesignerPage sellerDesignerPage) {
        return sellerDesignerPageMapper.queryLastOne(sellerDesignerPage);
    }
}
