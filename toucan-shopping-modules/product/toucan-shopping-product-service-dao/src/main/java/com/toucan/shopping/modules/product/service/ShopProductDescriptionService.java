package com.toucan.shopping.modules.product.service;



import com.toucan.shopping.modules.product.entity.ShopProductDescription;

public interface ShopProductDescriptionService {


    int save(ShopProductDescription entity);

    int deleteByShopProductId(Long productApproveId);

    ShopProductDescription queryByShopProductId(Long productApproveId);

}
