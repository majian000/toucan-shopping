package com.toucan.shopping.modules.product.service;

import com.toucan.shopping.modules.product.vo.ShopProductApproveSkuVO;

/**
 * 商品审核SKU redis服务类
 * @author majian
 */
public interface ShopProductApproveSkuRedisService {

    ShopProductApproveSkuVO queryProductApproveSku(String id);

    /**
     * 添加到缓存
     * @param shopProductApproveSkuVO
     */
    void addToCache(ShopProductApproveSkuVO shopProductApproveSkuVO);

    /**
     * 删除缓存
     * @param id
     */
    void deleteCache(String id);
}
