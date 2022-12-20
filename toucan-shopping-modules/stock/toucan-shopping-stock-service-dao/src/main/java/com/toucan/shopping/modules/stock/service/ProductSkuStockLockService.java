package com.toucan.shopping.modules.stock.service;



import com.toucan.shopping.modules.stock.entity.ProductSkuStockLock;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;

import java.util.List;


/**
 * 库存服务
 */
public interface ProductSkuStockLockService {


    int save(ProductSkuStockLock productSkuStockLock);

    int deletes(List<Long> idList);

    /**
     * 查询商品锁定库存
     * @param productSkuStockLockVO
     * @return
     */
    List<ProductSkuStockLockVO> queryStockNumByVO(ProductSkuStockLockVO productSkuStockLockVO);

}
