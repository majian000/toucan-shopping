package com.toucan.shopping.modules.admin.auth.service;




import com.toucan.shopping.modules.admin.auth.entity.AdminRole;

import java.util.List;

public interface AdminRoleService {

    List<AdminRole> findListByEntity(AdminRole adminRole);

    int save(AdminRole adminRole);

    int deleteByRoleId(Long roleId);

    List<AdminRole> findListByAdminId(String adminId);

}
