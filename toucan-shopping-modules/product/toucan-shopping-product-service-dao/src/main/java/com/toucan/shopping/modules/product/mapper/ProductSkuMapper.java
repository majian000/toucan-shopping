package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface ProductSkuMapper {

    List<ProductSkuVO> queryList(ProductSkuVO productSkuVO);

    ProductSku queryBySkuIdForUpdate(Long skuId);

    int insert(ProductSku productSku);

    int inserts(List<ProductSku> entitys);

    ProductSku queryById(Long id);

    ProductSku queryByUuid(String uuid);


    int deleteByShopProductId(Long shopProductId);

}
