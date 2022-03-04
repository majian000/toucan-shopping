package com.toucan.shopping.modules.admin.auth.service;




import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import com.toucan.shopping.modules.admin.auth.page.RoleFunctionPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.FunctionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.RoleFunctionVO;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.util.List;

public interface RoleFunctionService {

    /**
     * 根据查询对象查询列表
     * @param roleFunction
     * @return
     */
    List<RoleFunction> findListByEntity(RoleFunction roleFunction);

    /**
     * 保存角色功能项关联
     * @param roleFunction
     * @return
     */
    int save(RoleFunction roleFunction);

    /**
     * 批量保存角色功能项关联
     * @param roleFunctions
     * @return
     */
    int saves(RoleFunction[] roleFunctions);

    /**
     * 删除指定角色下的关联
     * @param roleId
     * @return
     */
    int deleteByRoleId(String roleId);

    /**
     * 删除指定功能项下的关联
     * @param functionId
     * @return
     */
    int deleteByFunctionId(String functionId);

    /**
     * 删除指定功能项下的关联
     * @param functionId
     * @return
     */
    int deleteByFunctionIdArray(String[] functionId);
    /**
     * 根据用户ID 权限URL 和应用编码查询所有角关联
     * @param url
     * @param appCode
     * @param roleIdArray
     * @return
     */
    List<RoleFunction> findListByAdminIdAndFunctionUrlAndAppCodeAndRoleIds(String url,String appCode,String[] roleIdArray);

    /**
     * 根据用户ID 权限URL 和应用编码查询所有角关联总数
     * @param url
     * @param appCode
     * @param roleIdArray
     * @return
     */
    Long findCountByAdminIdAndFunctionUrlAndAppCodeAndRoleIds(String url,String appCode,String[] roleIdArray);


    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<RoleFunction> queryListPage(RoleFunctionPageInfo queryPageInfo);


    /**
     * 查询到这个角色最终要关联的所有功能项
     * @param roleFunctionVO
     * @return
     */
    void queryReleaseFunctionList(RoleFunctionVO roleFunctionVO, List<FunctionTreeVO> functionTreeVOS);

}
