package com.toucan.shopping.modules.admin.auth.es.service;


import com.toucan.shopping.modules.admin.auth.vo.AdminRoleElasticSearchVO;

import java.util.List;

public interface AdminRoleElasticSearchService {

    /**
     * 保存对象
     * @param esVO
     */
    void save(AdminRoleElasticSearchVO esVO);

    /**
     * 更新对象
     * @param esVO
     */
    void update(AdminRoleElasticSearchVO esVO);

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
    List<AdminRoleElasticSearchVO> queryById(Long id) throws Exception;

    /**
     * 根据对象查询
     * @param query 查询条件对象
     * @param size 查询个数
     * @return
     * @throws Exception
     */
    List<AdminRoleElasticSearchVO> queryByEntity(AdminRoleElasticSearchVO query,Integer size) throws Exception;

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    boolean deleteById(String id) throws Exception;


}
