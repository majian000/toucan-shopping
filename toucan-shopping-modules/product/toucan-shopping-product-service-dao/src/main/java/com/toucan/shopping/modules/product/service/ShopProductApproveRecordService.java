package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.product.entity.ShopProductApproveRecord;

import java.util.List;

public interface ShopProductApproveRecordService {

    List<ShopProductApproveRecord> queryAllList(ShopProductApproveRecord queryModel);

}
