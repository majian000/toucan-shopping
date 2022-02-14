package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.product.entity.ShopProductSpuApproveRecord;
import com.toucan.shopping.modules.product.entity.ShopProductSpuImg;

import java.util.List;

public interface ShopProductSpuImgService {

    List<ShopProductSpuImg> queryAllList(ShopProductSpuImg queryModel);

}
