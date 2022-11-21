package com.toucan.shopping.modules.stock.service.impl;

import com.toucan.shopping.modules.stock.entity.ProductSkuStockLock;
import com.toucan.shopping.modules.stock.mapper.ProductSkuStockLockMapper;
import com.toucan.shopping.modules.stock.service.ProductSkuStockLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 库存服务
 */
@Service
public class ProductSkuStockLockServiceImpl implements ProductSkuStockLockService {

    @Autowired
    private ProductSkuStockLockMapper productSkuStockLockMapper;





    @Transactional
    @Override
    public int inventoryReduction(String skuUuid) {
        //锁住这条记录
        ProductSkuStockLock productSkuStockLock = productSkuStockLockMapper.queryBySkuUuidForUpdate(skuUuid);
        if(productSkuStockLock.getStockNum()>0) {
            return productSkuStockLockMapper.inventoryReduction(skuUuid);
        }
        return 0;
    }




    @Transactional
    @Override
    public int restoreStock(String skuUuid) {
        //锁住这条记录
        ProductSkuStockLock productSkuStockLock = productSkuStockLockMapper.queryBySkuUuidForUpdate(skuUuid);
        return productSkuStockLockMapper.restoreStock(skuUuid);
    }

    @Override
    public ProductSkuStockLock queryBySkuUuid(String skuUuid) {
        return productSkuStockLockMapper.queryBySkuUuid(skuUuid);
    }

}
