package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import com.toucan.shopping.modules.admin.auth.page.RoleFunctionPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.RoleFunctionListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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


    List<RoleFunction> queryListByRoleId(String roleId);

    /**
     * 批量查询: 根据角色ID数组和应用编码查询角色-功能关联
     */
    List<RoleFunction> findListByRoleIdsAndAppCode(@Param("roleIdArray") String[] roleIdArray, @Param("appCode") String appCode);


    /**
     * 查询角色功能列表(关联t_sa_function)
     */
    List<RoleFunctionListVO> queryRoleFunctionListPage(RoleFunctionPageInfo queryPageInfo);

    /**
     * 查询角色功能列表总数
     */
    Long queryRoleFunctionListPageCount(RoleFunctionPageInfo queryPageInfo);
}
