package com.toucan.shopping.modules.admin.auth.cache.service;


import com.toucan.shopping.modules.admin.auth.vo.AdminRoleCacheVO;

import java.util.List;

public interface AdminRoleCacheService {

    /**
     * 保存对象
     * @param esVO
     */
    void save(AdminRoleCacheVO esVO) throws Exception;

    /**
     * 更新对象
     * @param esVO
     */
    void update(AdminRoleCacheVO esVO) throws Exception;

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
    List<AdminRoleCacheVO> queryById(Long id) throws Exception;

    /**
     * 根据对象查询
     * @param query 查询条件对象
     * @return
     * @throws Exception
     */
    List<AdminRoleCacheVO> queryByEntity(AdminRoleCacheVO query) throws Exception;

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    boolean deleteById(String id) throws Exception;

    /**
     * 删除索引
     * @return
     * @throws Exception
     */
    boolean deleteIndex() throws Exception;

    /**
     * 删除指定账号下的指定所有应用下的所有账号角色关联
     * @param adminId
     * @param appCode
     * @return
     */
    boolean deleteByAdminIdAndAppCodes(String adminId,String appCode,List<String> deleteFaildIdList) throws Exception;

    /**
     * 批量保存
     * @param adminRoleCacheVOS
     * @return
     */
    void saves(AdminRoleCacheVO[] adminRoleCacheVOS) throws Exception;
}
