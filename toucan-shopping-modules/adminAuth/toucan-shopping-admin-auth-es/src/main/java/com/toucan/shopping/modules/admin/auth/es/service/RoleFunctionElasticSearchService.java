package com.toucan.shopping.modules.admin.auth.es.service;


import com.toucan.shopping.modules.admin.auth.vo.RoleFunctionElasticSearchVO;

import java.util.List;

public interface RoleFunctionElasticSearchService {

    /**
     * 保存对象
     * @param esVO
     */
    void save(RoleFunctionElasticSearchVO esVO);

    /**
     * 更新对象
     * @param esVO
     */
    void update(RoleFunctionElasticSearchVO esVO);

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
     * 根据ID查询
     * @param id
     * @return
     */
    List<RoleFunctionElasticSearchVO> queryById(Long id) throws Exception;

    /**
     * 根据对象查询
     * @param query 查询条件对象
     * @param size 查询个数
     * @return
     * @throws Exception
     */
    List<RoleFunctionElasticSearchVO> queryByEntity(RoleFunctionElasticSearchVO query, Integer size) throws Exception;

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    boolean deleteById(String id) throws Exception;


}
