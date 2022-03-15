package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.entity.ProductSpuAttributeKey;
import com.toucan.shopping.modules.product.mapper.ProductSpuAttributeKeyMapper;
import com.toucan.shopping.modules.product.service.ProductSpuAttributeKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductSpuAttributeKeyServiceImpl implements ProductSpuAttributeKeyService {


    @Autowired
    private ProductSpuAttributeKeyMapper productSpuAttributeKeyMapper;



    @Override
    public int save(ProductSpuAttributeKey entity) {
        return productSpuAttributeKeyMapper.insert(entity);
    }



}
