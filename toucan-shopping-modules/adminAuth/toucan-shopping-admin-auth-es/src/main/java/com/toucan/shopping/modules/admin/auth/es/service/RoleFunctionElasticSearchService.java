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
     * @return
     * @throws Exception
     */
    List<RoleFunctionElasticSearchVO> queryByEntity(RoleFunctionElasticSearchVO query) throws Exception;

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    boolean deleteById(String id) throws Exception;

    /**
     * 根据角色ID删除
     * @param roleId
     * @return
     * @throws Exception
     */
    boolean deleteByRoleId(String roleId,List<String> deleteFaildIdList) throws Exception;

    /**
     * 根据功能项ID删除
     * @param functionId
     * @param deleteFaildIdList
     * @return
     */
    boolean deleteByFunctionId(String functionId,List<String> deleteFaildIdList) throws Exception;

    /**
     * 批量保存
     * @param roleFunctionElasticSearchVOS
     * @return
     */
    void saves(RoleFunctionElasticSearchVO[] roleFunctionElasticSearchVOS);

}
