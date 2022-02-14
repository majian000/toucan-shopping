package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ProductSpu;
import com.toucan.shopping.modules.product.entity.ShopProductSpu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ShopProductSpuMapper {

    List<ShopProductSpu> queryAllList(ShopProductSpu queryModel);

}
