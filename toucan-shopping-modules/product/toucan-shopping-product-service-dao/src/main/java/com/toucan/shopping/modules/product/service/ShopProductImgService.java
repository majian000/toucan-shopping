package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.product.entity.ShopProductImg;
import com.toucan.shopping.modules.product.vo.ShopProductImgVO;

import java.util.List;

public interface ShopProductImgService {

    List<ShopProductImg> queryAllList(ShopProductImg queryModel);


    List<ShopProductImg> queryList(ShopProductImgVO queryModel);

    int saves(List<ShopProductImg> entitys);


    List<ShopProductImg> queryByIdList(List<Long> ids);

}
