package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ProductSpu;
import com.toucan.shopping.modules.product.page.ProductSpuPageInfo;
import com.toucan.shopping.modules.product.vo.ProductSpuVO;

import java.util.List;

public interface ProductSpuService {

    List<ProductSpu> queryAllList(ProductSpu queryModel);


    int save(ProductSpu entity);

    List<ProductSpuVO> queryList(ProductSpuVO query);


    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<ProductSpuVO> queryListPage(ProductSpuPageInfo queryPageInfo);


}
