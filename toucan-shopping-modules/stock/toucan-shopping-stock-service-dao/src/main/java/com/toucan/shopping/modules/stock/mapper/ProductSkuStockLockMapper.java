package com.toucan.shopping.modules.stock.mapper;

import com.toucan.shopping.modules.stock.entity.ProductSkuStockLock;
import com.toucan.shopping.modules.stock.page.ProductSkuStockLockPageInfo;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ProductSkuStockLockMapper {


    int save(ProductSkuStockLock productSkuStockLock);

    int deletes(List<Long> idList);

    List<ProductSkuStockLockVO> queryStockNumByVO(ProductSkuStockLockVO productSkuStockLockVO);

    List<ProductSkuStockLockVO> queryListByVO(ProductSkuStockLockVO productSkuStockLockVO);

    ProductSkuStockLockVO findById(Long id);

    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<ProductSkuStockLockVO> queryListPage(ProductSkuStockLockPageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(ProductSkuStockLockPageInfo pageInfo);


}
