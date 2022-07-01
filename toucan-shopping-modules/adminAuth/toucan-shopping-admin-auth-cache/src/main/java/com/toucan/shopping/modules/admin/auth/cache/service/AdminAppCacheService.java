package com.toucan.shopping.modules.admin.auth.cache.service;


import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;

public interface AdminAppCacheService {


    /**
     * 保存对象
     * @param adminAppVO
     */
    void save(AdminAppVO adminAppVO) throws Exception;

    /**
     * 根据应用编码移除
     * @param appCode
     * @return
     * @throws Exception
     */
    boolean deleteByAdminIdAndAppCode(String adminId,String appCode)  throws Exception;

    /**
     * 根据管理员账号移除
     * @param adminId
     * @return
     */
    boolean deleteByAdminId(String adminId);

    /**
     * 根据账号ID和应用编码查询
     * @param appCode
     * @return
     * @throws Exception
     */
    AdminAppVO findByAdminIdAndAppCode(String adminId,String appCode)  throws Exception;
}
