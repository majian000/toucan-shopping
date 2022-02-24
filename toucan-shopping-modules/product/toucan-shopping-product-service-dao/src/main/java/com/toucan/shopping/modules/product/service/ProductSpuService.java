package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.product.entity.ProductSpu;
import com.toucan.shopping.modules.product.vo.ProductSpuVO;

import java.util.List;

public interface ProductSpuService {

    List<ProductSpu> queryAllList(ProductSpu queryModel);


    int save(ProductSpu entity);

    List<ProductSpuVO> queryList(ProductSpuVO query);

}
