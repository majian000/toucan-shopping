package com.toucan.shopping.modules.stock.service;



import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.stock.entity.ProductSkuStockLock;
import com.toucan.shopping.modules.stock.page.ProductSkuStockLockPageInfo;
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

    /**
     * 查询列表
     * @param productSkuStockLockVO
     * @return
     */
    List<ProductSkuStockLockVO> queryListByVO(ProductSkuStockLockVO productSkuStockLockVO);


    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    PageInfo<ProductSkuStockLockVO> queryListPage(ProductSkuStockLockPageInfo pageInfo);


}
