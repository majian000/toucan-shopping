package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.product.entity.ShopProduct;

import java.util.List;

public interface ShopProductService {

    List<ShopProduct> queryAllList(ShopProduct queryModel);

    int save(ShopProduct entity);
}
