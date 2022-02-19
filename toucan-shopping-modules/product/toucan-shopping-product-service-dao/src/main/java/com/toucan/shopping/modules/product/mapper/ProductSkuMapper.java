package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ProductSku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface ProductSkuMapper {

    List<ProductSku> queryList(Map<String,Object> queryMap);

    ProductSku queryBySkuIdForUpdate(Long skuId);

    int insert(ProductSku productSku);

    int inserts(List<ProductSku> entitys);

    ProductSku queryById(Long id);

    ProductSku queryByUuid(String uuid);
}
