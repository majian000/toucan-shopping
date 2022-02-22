package com.toucan.shopping.modules.product.service;



import com.toucan.shopping.modules.product.entity.ProductSku;

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

    int saves(List<ProductSku> productSkus);

    ProductSku queryById(Long id);

    ProductSku queryByUuid(String uuid);


    int deleteByShopProductId(Long shopProductId);


}
