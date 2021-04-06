package com.toucan.shopping.admin.auth.service;




import com.toucan.shopping.admin.auth.entity.AdminRole;

import java.util.List;

public interface AdminRoleService {

    List<AdminRole> findListByEntity(AdminRole adminRole);

    int save(AdminRole adminRole);

    int deleteByRoleId(Long roleId);

}
