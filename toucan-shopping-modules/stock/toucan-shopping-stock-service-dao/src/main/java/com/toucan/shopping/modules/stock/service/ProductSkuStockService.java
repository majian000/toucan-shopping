package com.toucan.shopping.modules.stock.service;



import com.toucan.shopping.modules.stock.entity.ProductSkuStock;

import java.util.List;
import java.util.Map;


/**
 * 库存服务
 */
public interface ProductSkuStockService {

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



    ProductSkuStock queryBySkuUuid(String skuUuid);

}
