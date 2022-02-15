package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.entity.ShopProduct;
import com.toucan.shopping.modules.product.mapper.ShopProductMapper;
import com.toucan.shopping.modules.product.service.ShopProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopProductServiceImpl implements ShopProductService {

    @Autowired
    private ShopProductMapper shopProductSpuMapper;

    @Override
    public List<ShopProduct> queryAllList(ShopProduct queryModel) {
        return shopProductSpuMapper.queryAllList(queryModel);
    }
}
