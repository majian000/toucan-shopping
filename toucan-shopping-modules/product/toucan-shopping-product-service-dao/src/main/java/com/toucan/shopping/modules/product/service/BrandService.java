package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.product.entity.Brand;

import java.util.List;

public interface BrandService {

    List<Brand> queryAllList(Brand queryModel);


    int save(Brand entity);

}
