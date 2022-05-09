package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ShopProductApproveDescription;
import com.toucan.shopping.modules.product.entity.ShopProductApproveSku;
import com.toucan.shopping.modules.product.mapper.ShopProductApproveDescriptionMapper;
import com.toucan.shopping.modules.product.mapper.ShopProductApproveSkuMapper;
import com.toucan.shopping.modules.product.page.ShopProductApproveSkuPageInfo;
import com.toucan.shopping.modules.product.service.ShopProductApproveDescriptionService;
import com.toucan.shopping.modules.product.service.ShopProductApproveSkuService;
import com.toucan.shopping.modules.product.vo.ShopProductApproveSkuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ShopProductApproveDescriptionServiceImpl implements ShopProductApproveDescriptionService {

    @Autowired
    private ShopProductApproveDescriptionMapper shopProductApproveDescriptionMapper;

    @Override
    public int save(ShopProductApproveDescription entity) {
        return shopProductApproveDescriptionMapper.insert(entity);
    }

    @Override
    public int deleteByShopProductApproveId(Long productApproveId) {
        return shopProductApproveDescriptionMapper.deleteByShopProductApproveId(productApproveId);
    }
}
