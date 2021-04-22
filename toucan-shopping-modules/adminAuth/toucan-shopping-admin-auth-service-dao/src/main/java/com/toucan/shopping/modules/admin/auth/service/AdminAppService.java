package com.toucan.shopping.modules.admin.auth.service;




import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;

import java.util.List;

public interface AdminAppService {

    /**
     * 根据实体查询列表
     * @param adminApp
     * @return
     */
    List<AdminApp> findListByEntity(AdminApp adminApp);

    /**
     * 保存关联
     * @param adminApp
     * @return
     */
    int save(AdminApp adminApp);

    /**
     * 删除指定应用编码下所有关联
     * @param appCode
     * @return
     */
    int deleteByAppCode(String appCode);

    /**
     * 删除指定账号下所有关联
     * @param adminId
     * @return
     */
    int deleteByAdminId(String adminId);

    /**
     * 查询账号应用关联列表
     * @param query
     * @return
     */
    List<AdminAppVO> findAppListByAdminAppEntity(AdminApp query);



    /**
     * 删除指定账号下指定应用的关联
     * @param adminId
     * @return
     */
    int deleteByAdminIdAndAppCode(String adminId,String appCode);
}
