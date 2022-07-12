package com.toucan.shopping.cloud.product.service;

import com.toucan.shopping.modules.product.vo.ProductSkuVO;

/**
 * 商品SKU redis服务类
 * @author majian
 */
public interface ProductSkuRedisService {

    ProductSkuVO queryProductSku(String id);

    /**
     * 添加到缓存
     * @param shopProductSkuVO
     */
    void addToCache(ProductSkuVO shopProductSkuVO);

    /**
     * 删除缓存
     * @param id
     */
    void deleteCache(String id);
}
