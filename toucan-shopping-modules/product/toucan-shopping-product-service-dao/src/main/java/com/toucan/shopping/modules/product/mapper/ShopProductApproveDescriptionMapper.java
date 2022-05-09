package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ShopProductApproveDescription;
import com.toucan.shopping.modules.product.entity.ShopProductDescription;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ShopProductApproveDescriptionMapper {

    int insert(ShopProductApproveDescription entity);

    int deleteByShopProductApproveId(Long productApproveId);
}
