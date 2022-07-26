package com.toucan.shopping.modules.admin.auth.service;


import com.toucan.shopping.modules.admin.auth.entity.Role;
import com.toucan.shopping.modules.admin.auth.page.RolePageInfo;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.util.List;

public interface RoleService {

    List<Role> findListByEntity(Role role);

    int save(Role Role);

    int update(Role Role);

    boolean exists(String name);

    PageInfo<Role> queryListPage(RolePageInfo RolePageInfo);

    int deleteById(Long id);
}
