package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import com.toucan.shopping.modules.admin.auth.page.RoleFunctionPageInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface RoleFunctionMapper {

    int insert(RoleFunction entity);

    int inserts(RoleFunction[] entitys);

    List<RoleFunction> findListByEntity(RoleFunction entity);

    int deleteByRoleId(String roleId);

    int deleteByFunctionId(String functionId);

    int deleteByFunctionIdArray(String[] functionIdArray);

    List<RoleFunction> findListByAdminIdAndFunctionUrlAndAppCodeAndRoleIds(String url,String appCode,String[] roleIdArray);

    Long findCountByAdminIdAndFunctionUrlAndAppCodeAndRoleIds(String url,String appCode,String[] roleIdArray);


    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    List<RoleFunction> queryListPage(RoleFunctionPageInfo queryPageInfo);

    /**
     * 返回列表页数量
     * @param queryPageInfo
     * @return
     */
    Long queryListPageCount(RoleFunctionPageInfo queryPageInfo);
}
