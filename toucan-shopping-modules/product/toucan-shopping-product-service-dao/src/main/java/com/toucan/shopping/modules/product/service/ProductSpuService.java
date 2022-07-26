package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ProductSpu;
import com.toucan.shopping.modules.product.page.ProductSpuPageInfo;
import com.toucan.shopping.modules.product.vo.ProductSpuVO;

import java.util.List;

public interface ProductSpuService {

    List<ProductSpu> queryAllList(ProductSpu queryModel);


    int save(ProductSpu entity);


    int update(ProductSpuVO entity);

    List<ProductSpuVO> queryList(ProductSpuVO query);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<ProductSpuVO> queryListPage(ProductSpuPageInfo queryPageInfo);

    ProductSpu queryByIdIgnoreDelete(Long id);


}
