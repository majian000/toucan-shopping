package com.toucan.shopping.modules.stock.service;



import com.toucan.shopping.modules.stock.entity.ProductSkuStockLock;


/**
 * 库存服务
 */
public interface ProductSkuStockLockService {

    /**
     * 删减库存
     * @param skuUuid
     * @return
     */
    int inventoryReduction(String skuUuid);



    /**
     * 恢复库存
     * @param skuUuid
     * @return
     */
    int restoreStock(String skuUuid);



    ProductSkuStockLock queryBySkuUuid(String skuUuid);

}
