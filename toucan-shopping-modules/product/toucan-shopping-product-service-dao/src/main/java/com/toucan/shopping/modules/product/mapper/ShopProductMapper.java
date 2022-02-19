package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ShopProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ShopProductMapper {

    List<ShopProduct> queryAllList(ShopProduct queryModel);

    int insert(ShopProduct entity);

    int deleteById(Long id);
}
