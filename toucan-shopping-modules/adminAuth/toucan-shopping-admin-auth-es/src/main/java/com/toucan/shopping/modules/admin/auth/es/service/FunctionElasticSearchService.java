package com.toucan.shopping.modules.admin.auth.es.service;


import com.toucan.shopping.modules.admin.auth.vo.FunctionElasticSearchVO;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;

public interface FunctionElasticSearchService {

    /**
     * 保存对象
     * @param esVO
     */
    void save(FunctionElasticSearchVO esVO) throws Exception;

    /**
     * 更新对象
     * @param esVO
     */
    void update(FunctionElasticSearchVO esVO) throws Exception;

    /**
     * 是否存在索引
     * @return
     */
    boolean existsIndex();

    /**
     * 创建索引
     */
    void createIndex();

    /**
     * 删除索引
     * @return
     * @throws Exception
     */
    boolean deleteIndex() throws Exception;

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    List<FunctionElasticSearchVO> queryById(Long id) throws Exception;

    /**
     * 根据对象查询
     * @param query 查询条件对象
     * @return
     * @throws Exception
     */
    List<FunctionElasticSearchVO> queryByEntity(FunctionElasticSearchVO query) throws Exception;

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    boolean deleteById(String id) throws Exception;

    /**
     * 查询记录总数量
     * @param searchSourceBuilder
     * @return
     * @throws Exception
     */
    Long queryCount(SearchSourceBuilder searchSourceBuilder)  throws Exception;

}
