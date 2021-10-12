package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.entity.BrandCategory;

import java.util.List;

public interface BrandCategoryService {

    List<BrandCategory> queryAllList(BrandCategory queryModel);

}
