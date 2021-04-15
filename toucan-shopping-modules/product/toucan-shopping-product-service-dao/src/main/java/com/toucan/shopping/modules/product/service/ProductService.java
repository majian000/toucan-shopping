package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.product.entity.Product;

import java.util.List;

public interface ProductService {

    public List<Product> queryAllList(Product queryModel);

}
