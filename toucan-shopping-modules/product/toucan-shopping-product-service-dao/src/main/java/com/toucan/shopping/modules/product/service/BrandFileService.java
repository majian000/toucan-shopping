package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.entity.BrandFile;

import java.util.List;

/**
 * 品牌证明材料表
 */
public interface BrandFileService {

    List<BrandFile> queryAllList(BrandFile queryModel);

}
