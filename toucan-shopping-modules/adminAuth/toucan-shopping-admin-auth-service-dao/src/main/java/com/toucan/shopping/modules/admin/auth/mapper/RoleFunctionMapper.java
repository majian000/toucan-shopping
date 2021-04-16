package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface RoleFunctionMapper {

    int insert(RoleFunction entity);

    List<RoleFunction> findListByEntity(RoleFunction entity);

    int deleteByRoleId(Long roleId);

    int deleteByFunctionId(Long functionId);

}
