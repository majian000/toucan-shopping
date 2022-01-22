package com.toucan.shopping.modules.admin.auth.cache.service;


import com.toucan.shopping.modules.admin.auth.vo.RoleFunctionCacheVO;

import java.util.List;

public interface RoleFunctionCacheService {

    /**
     * 保存对象
     * @param esVO
     */
    void save(RoleFunctionCacheVO esVO) throws Exception;

    /**
     * 更新对象
     * @param esVO
     */
    void update(RoleFunctionCacheVO esVO) throws Exception;

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
    List<RoleFunctionCacheVO> queryById(Long id) throws Exception;

    /**
     * 根据对象查询
     * @param query 查询条件对象
     * @return
     * @throws Exception
     */
    List<RoleFunctionCacheVO> queryByEntity(RoleFunctionCacheVO query) throws Exception;

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
     * @param roleFunctionCacheVOS
     * @return
     */
    void saves(RoleFunctionCacheVO[] roleFunctionCacheVOS) throws Exception;



}
