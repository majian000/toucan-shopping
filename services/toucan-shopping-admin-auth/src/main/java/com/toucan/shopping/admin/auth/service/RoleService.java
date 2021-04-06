package com.toucan.shopping.admin.auth.service;


import com.github.pagehelper.PageInfo;
import com.toucan.shopping.admin.auth.entity.Role;
import com.toucan.shopping.admin.auth.page.RolePageInfo;

import java.util.List;

public interface RoleService {

    List<Role> findListByEntity(Role role);

    int save(Role Role);

    int update(Role Role);

    boolean exists(String name);

    PageInfo<Role> queryListPage(RolePageInfo RolePageInfo);

    int deleteById(Long id);
}
