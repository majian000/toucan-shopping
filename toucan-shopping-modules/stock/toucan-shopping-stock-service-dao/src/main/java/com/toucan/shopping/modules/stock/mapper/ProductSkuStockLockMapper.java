package com.toucan.shopping.modules.stock.mapper;

import com.toucan.shopping.modules.stock.entity.ProductSkuStockLock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ProductSkuStockLockMapper {

    int inventoryReduction(String skuUuid);

    int restoreStock(String skuUuid);

    ProductSkuStockLock queryBySkuUuidForUpdate(String skuUuid);

    ProductSkuStockLock queryBySkuUuid(String skuUuid);

    int save(ProductSkuStockLock productSkuStockLock);

    int deletes(List<Long> idList);


}
