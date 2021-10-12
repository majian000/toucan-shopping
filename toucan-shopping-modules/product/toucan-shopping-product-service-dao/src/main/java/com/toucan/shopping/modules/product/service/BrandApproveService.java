package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.product.entity.BrandApprove;

import java.util.List;

public interface BrandApproveService {

    List<BrandApprove> queryAllList(BrandApprove queryModel);

}
