package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.product.entity.ShopProductImg;

import java.util.List;

public interface ShopProductImgService {

    List<ShopProductImg> queryAllList(ShopProductImg queryModel);


    int saves(List<ShopProductImg> entitys);

}
