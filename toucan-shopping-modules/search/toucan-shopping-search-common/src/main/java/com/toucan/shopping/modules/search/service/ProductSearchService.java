package com.toucan.shopping.modules.search.service;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import com.toucan.shopping.modules.search.vo.ProductSearchVO;

import java.io.IOException;
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
    PageInfo<ProductSearchResultVO> search(ProductSearchVO productSearchVO);

    /**
     * 保存商品到搜索中间件
     * @param productSearchResultVO
     */
    void save(ProductSearchResultVO productSearchResultVO) throws IOException;

    /**
     * 是否存在索引
     * @return
     */
    boolean existsIndex();



    /**
     * 根据ID查询
     * @param id
     * @return
     */
    List<ProductSearchResultVO> queryBySkuId(Long id) throws Exception;


}
