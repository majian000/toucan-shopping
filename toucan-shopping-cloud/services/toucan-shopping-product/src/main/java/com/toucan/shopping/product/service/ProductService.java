package com.toucan.shopping.product.service;

import com.toucan.shopping.product.entity.Product;

import java.util.List;

public interface ProductService {

    public List<Product> queryAllList(Product queryModel);

}
