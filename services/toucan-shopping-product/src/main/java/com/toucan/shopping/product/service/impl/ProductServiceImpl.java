package com.toucan.shopping.product.service.impl;

import com.toucan.shopping.product.entity.Product;
import com.toucan.shopping.product.mapper.ProductMapper;
import com.toucan.shopping.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> queryAllList(Product queryModel) {
        return productMapper.queryAllList(queryModel);
    }
}
