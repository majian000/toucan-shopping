package com.toucan.shopping.modules.stock.service.impl;

import com.toucan.shopping.modules.stock.entity.ProductSkuStock;
import com.toucan.shopping.modules.stock.mapper.ProductSkuStockMapper;
import com.toucan.shopping.modules.stock.service.ProductSkuStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 库存服务
 */
@Service
public class ProductSkuStockServiceImpl implements ProductSkuStockService {

    @Autowired
    private ProductSkuStockMapper productSkuStockMapper;





    @Transactional
    @Override
    public int inventoryReduction(String skuUuid) {
        //锁住这条记录
        ProductSkuStock productSkuStock = productSkuStockMapper.queryBySkuUuidForUpdate(skuUuid);
        if(productSkuStock.getStockNum()>0) {
            return productSkuStockMapper.inventoryReduction(skuUuid);
        }
        return 0;
    }




    @Transactional
    @Override
    public int restoreStock(String skuUuid) {
        //锁住这条记录
        ProductSkuStock productSkuStock = productSkuStockMapper.queryBySkuUuidForUpdate(skuUuid);
        return productSkuStockMapper.restoreStock(skuUuid);
    }

    @Override
    public ProductSkuStock queryBySkuUuid(String skuUuid) {
        return productSkuStockMapper.queryBySkuUuid(skuUuid);
    }

}
