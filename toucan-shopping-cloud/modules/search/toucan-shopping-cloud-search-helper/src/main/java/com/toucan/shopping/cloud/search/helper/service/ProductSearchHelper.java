package com.toucan.shopping.cloud.search.helper.service;


import com.toucan.shopping.modules.product.vo.ProductSkuVO;

/**
 * 商品搜索同步
 * @author majian
 */
public interface ProductSearchHelper {

    /**
     * 刷新搜索
     * @param productSkuVO
     */
    void refresh(ProductSkuVO productSkuVO) throws Exception;

}
