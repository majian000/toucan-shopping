package com.toucan.shopping.product.service.impl;

import com.toucan.shopping.product.entity.ProductSku;
import com.toucan.shopping.product.mapper.ProductSkuMapper;
import com.toucan.shopping.product.service.ProductSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ProductSkuServiceImpl implements ProductSkuService {

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<ProductSku> queryList(Map<String,Object> queryMap) {
        return productSkuMapper.queryList(queryMap);
    }



    @Transactional
    @Override
    public int save(ProductSku productSku) {
        return productSkuMapper.insert(productSku);
    }

    @Override
    public ProductSku queryById(Long id) {
        return productSkuMapper.queryById(id);
    }

    @Override
    public ProductSku queryByUuid(String uuid) {
        return productSkuMapper.queryByUuid(uuid);
    }
}
