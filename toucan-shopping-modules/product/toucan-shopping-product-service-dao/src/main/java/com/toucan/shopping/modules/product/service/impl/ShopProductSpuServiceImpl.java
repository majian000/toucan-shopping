package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.entity.ProductSpu;
import com.toucan.shopping.modules.product.entity.ShopProductSpu;
import com.toucan.shopping.modules.product.mapper.ShopProductSpuMapper;
import com.toucan.shopping.modules.product.service.ProductSpuService;
import com.toucan.shopping.modules.product.service.ShopProductSpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopProductSpuServiceImpl implements ShopProductSpuService {

    @Autowired
    private ShopProductSpuMapper shopProductSpuMapper;

    @Override
    public List<ShopProductSpu> queryAllList(ShopProductSpu queryModel) {
        return shopProductSpuMapper.queryAllList(queryModel);
    }
}
