package com.toucan.shopping.modules.product.service;



import com.toucan.shopping.modules.product.entity.ShopProductApproveDescriptionImg;

import java.util.List;

public interface ShopProductApproveDescriptionImgService {

    int saves(List<ShopProductApproveDescriptionImg> entitys);

    int deleteByProductApproveId(Long productApproveId);

}
