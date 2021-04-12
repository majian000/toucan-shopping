package com.toucan.shopping.stock.mapper;

import com.toucan.shopping.stock.entity.ProductSkuStock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface ProductSkuStockMapper {

    int inventoryReduction(String skuUuid);

    int restoreStock(String skuUuid);

    ProductSkuStock queryBySkuUuidForUpdate(String skuUuid);

    ProductSkuStock queryBySkuUuid(String skuUuid);

}
