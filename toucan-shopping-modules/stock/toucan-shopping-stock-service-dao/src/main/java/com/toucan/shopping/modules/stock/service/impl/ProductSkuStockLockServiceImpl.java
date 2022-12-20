package com.toucan.shopping.modules.stock.service.impl;

import com.toucan.shopping.modules.stock.entity.ProductSkuStockLock;
import com.toucan.shopping.modules.stock.mapper.ProductSkuStockLockMapper;
import com.toucan.shopping.modules.stock.service.ProductSkuStockLockService;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 库存服务
 */
@Service
public class ProductSkuStockLockServiceImpl implements ProductSkuStockLockService {

    @Autowired
    private ProductSkuStockLockMapper productSkuStockLockMapper;



    @Override
    public int save(ProductSkuStockLock productSkuStockLock) {
        return productSkuStockLockMapper.save(productSkuStockLock);
    }

    @Override
    public int deletes(List<Long> idList) {
        return productSkuStockLockMapper.deletes(idList);
    }

    @Override
    public List<ProductSkuStockLockVO> queryStockNumByVO(ProductSkuStockLockVO productSkuStockLockVO) {
        return productSkuStockLockMapper.queryStockNumByVO(productSkuStockLockVO);
    }


}
