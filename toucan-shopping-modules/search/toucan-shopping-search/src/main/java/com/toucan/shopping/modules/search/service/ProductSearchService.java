package com.toucan.shopping.modules.search.service;

import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import com.toucan.shopping.modules.search.vo.ProductSearchVO;

import java.util.List;

/**
 * 商品搜索
 */
public interface ProductSearchService {

    /**
     * 创建索引
     */
    void createIndex();


    /**
     * 搜索商品
     * @param productSearchVO
     * @return
     */
    List<ProductSearchResultVO> search(ProductSearchVO productSearchVO);


}
