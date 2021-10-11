package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.entity.ProductSpu;
import com.toucan.shopping.modules.product.mapper.ProductSpuMapper;
import com.toucan.shopping.modules.product.service.ProductSpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSpuServiceImpl implements ProductSpuService {

    @Autowired
    private ProductSpuMapper productSpuMapper;

    @Override
    public List<ProductSpu> queryAllList(ProductSpu queryModel) {
        return productSpuMapper.queryAllList(queryModel);
    }
}
