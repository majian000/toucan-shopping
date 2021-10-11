package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.product.entity.ProductSpu;

import java.util.List;

public interface ProductSpuService {

    public List<ProductSpu> queryAllList(ProductSpu queryModel);

}
