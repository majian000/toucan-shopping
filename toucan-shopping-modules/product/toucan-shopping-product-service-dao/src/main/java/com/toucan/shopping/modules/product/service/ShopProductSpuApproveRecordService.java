package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.product.entity.ShopProductSpu;
import com.toucan.shopping.modules.product.entity.ShopProductSpuApproveRecord;

import java.util.List;

public interface ShopProductSpuApproveRecordService {

    List<ShopProductSpuApproveRecord> queryAllList(ShopProductSpuApproveRecord queryModel);

}
