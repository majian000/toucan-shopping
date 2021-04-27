package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface RoleFunctionMapper {

    int insert(RoleFunction entity);

    int inserts(RoleFunction[] entitys);

    List<RoleFunction> findListByEntity(RoleFunction entity);

    int deleteByRoleId(String roleId);

    int deleteByFunctionId(String functionId);

    List<RoleFunction> findListByAdminIdAndFunctionUrlAndAppCodeAndRoleIds(String url,String appCode,String[] roleIdArray);

    Long findCountByAdminIdAndFunctionUrlAndAppCodeAndRoleIds(String url,String appCode,String[] roleIdArray);

}
