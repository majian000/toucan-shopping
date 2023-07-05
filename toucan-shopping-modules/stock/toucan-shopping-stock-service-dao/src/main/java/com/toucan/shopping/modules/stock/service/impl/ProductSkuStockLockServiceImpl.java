package com.toucan.shopping.modules.stock.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.stock.entity.ProductSkuStockLock;
import com.toucan.shopping.modules.stock.mapper.ProductSkuStockLockMapper;
import com.toucan.shopping.modules.stock.page.ProductSkuStockLockPageInfo;
import com.toucan.shopping.modules.stock.service.ProductSkuStockLockService;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<ProductSkuStockLockVO> queryListByVO(ProductSkuStockLockVO productSkuStockLockVO) {
        return productSkuStockLockMapper.queryListByVO(productSkuStockLockVO);
    }

    @Override
    public PageInfo<ProductSkuStockLockVO> queryListPage(ProductSkuStockLockPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<ProductSkuStockLockVO> pageInfo = new PageInfo();
        pageInfo.setList(productSkuStockLockMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(productSkuStockLockMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }


}
