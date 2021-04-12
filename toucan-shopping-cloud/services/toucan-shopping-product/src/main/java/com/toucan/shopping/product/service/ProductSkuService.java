package com.toucan.shopping.product.service;


import com.toucan.shopping.product.entity.ProductSku;

import java.util.List;
import java.util.Map;

public interface ProductSkuService {

    List<ProductSku> queryList(Map<String,Object> queryMap);


    /**
     * 保存sku
     * @param productSku
     * @return
     */
    int save(ProductSku productSku);

    ProductSku queryById(Long id);


    ProductSku queryByUuid(String uuid);

}
