package com.toucan.shopping.modules.search.service;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import com.toucan.shopping.modules.search.vo.ProductSearchVO;
import org.elasticsearch.search.builder.SearchSourceBuilder;

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
    PageInfo<ProductSearchResultVO> search(ProductSearchVO productSearchVO) throws Exception;

    /**
     * 查询数量
     * @param searchSourceBuilder
     * @return
     * @throws Exception
     */
    Long queryCount(SearchSourceBuilder searchSourceBuilder)  throws Exception;

    /**
     * 保存商品到搜索中间件
     * @param productSearchResultVO
     */
    void save(ProductSearchResultVO productSearchResultVO) throws IOException;

    /**
     * 修改商品
     * @param productSearchResultVO
     * @throws IOException
     */
    void update(ProductSearchResultVO productSearchResultVO) throws IOException, IllegalAccessException;

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
