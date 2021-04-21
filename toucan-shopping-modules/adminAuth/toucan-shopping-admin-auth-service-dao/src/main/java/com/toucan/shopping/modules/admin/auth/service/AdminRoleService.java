package com.toucan.shopping.modules.admin.auth.service;




import com.toucan.shopping.modules.admin.auth.entity.AdminRole;

import java.util.List;

public interface AdminRoleService {

    List<AdminRole> findListByEntity(AdminRole adminRole);

    int save(AdminRole adminRole);

    int deleteByRoleId(String roleId);

    int deleteByAdminId(String adminId);

    /**
     * 删除指定账号下的指定所有应用下的所有账号角色关联
     * @param adminId
     * @param appCodes
     * @return
     */
    int deleteByAdminIdAndAppCodes(String adminId,String[] appCodes);

    List<AdminRole> findListByAdminId(String adminId);

}
