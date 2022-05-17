package com.toucan.shopping.modules.product.service;



import com.toucan.shopping.modules.product.entity.ShopProductDescription;

public interface ShopProductDescriptionService {


    int save(ShopProductDescription entity);

    int deleteByShopProductApproveId(Long productApproveId);

    ShopProductDescription queryByShopProductId(Long productApproveId);

}
