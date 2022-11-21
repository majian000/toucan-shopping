package com.toucan.shopping.modules.stock.service;



import com.toucan.shopping.modules.stock.entity.ProductSkuStockLock;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;

import java.util.List;


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

    int save(ProductSkuStockLock productSkuStockLock);

    int deletes(List<Long> idList);

}
