package com.toucan.shopping.modules.admin.auth.cache.service;


import com.toucan.shopping.modules.admin.auth.vo.FunctionCacheVO;

import java.util.List;

public interface FunctionCacheService {

    /**
     * 保存对象
     * @param esVO
     */
    void save(FunctionCacheVO esVO) throws Exception;

    /**
     * 更新对象
     * @param esVO
     */
    void update(FunctionCacheVO esVO) throws Exception;

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
    List<FunctionCacheVO> queryById(Long id) throws Exception;

    /**
     * 根据对象查询
     * @param query 查询条件对象
     * @return
     * @throws Exception
     */
    List<FunctionCacheVO> queryByEntity(FunctionCacheVO query) throws Exception;

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    boolean deleteById(String id) throws Exception;


}
