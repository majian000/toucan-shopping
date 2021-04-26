package com.toucan.shopping.modules.admin.auth.service;




import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;

import java.util.List;

public interface RoleFunctionService {

    List<RoleFunction> findListByEntity(RoleFunction roleFunction);

    int save(RoleFunction roleFunction);

    int saves(RoleFunction[] roleFunctions);

    int deleteByRoleId(String roleId);

    int deleteByFunctionId(String functionId);

    List<RoleFunction> findListByAdminIdAndFunctionUrlAndAppCodeAndRoleIds(String url,String appCode,String[] roleIdArray);
}
