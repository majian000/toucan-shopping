package com.toucan.shopping.modules.admin.auth.service;




import com.toucan.shopping.modules.admin.auth.entity.AdminOrgnazition;
import com.toucan.shopping.modules.admin.auth.vo.AdminOrgnazitionVO;

import java.util.List;

public interface AdminOrgnazitionService {

    /**
     * 根据实体查询列表
     * @param entity
     * @return
     */
    List<AdminOrgnazition> findListByEntity(AdminOrgnazition entity);

    /**
     * 保存关联
     * @param entity
     * @return
     */
    int save(AdminOrgnazition entity);

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
     * 根据组织机构ID删除
     * @param orgnazitionId
     * @return
     */
    int deleteByOrgnazitionId(String orgnazitionId);


    /**
     * 删除指定账号下指定应用的关联
     * @param adminId
     * @return
     */
    int deleteByAdminIdAndAppCode(String adminId, String appCode);



    /**
     * 删除指定账号下的指定所有应用下的所有账号角色关联
     * @param adminId
     * @param appCodes
     * @return
     */
    int deleteByAdminIdAndAppCodes(String adminId,String[] appCodes);



    int saves(AdminOrgnazition[] adminOrgnazitionMapper);
}
