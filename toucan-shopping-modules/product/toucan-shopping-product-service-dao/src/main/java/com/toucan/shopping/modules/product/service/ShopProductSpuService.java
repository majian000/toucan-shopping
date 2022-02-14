package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.product.entity.ProductSpu;
import com.toucan.shopping.modules.product.entity.ShopProductSpu;

import java.util.List;

public interface ShopProductSpuService {

    public List<ShopProductSpu> queryAllList(ShopProductSpu queryModel);

}
