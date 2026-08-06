package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.Role;
import com.toucan.shopping.modules.admin.auth.page.RolePageInfo;
import com.toucan.shopping.modules.admin.auth.vo.RoleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface RoleMapper {

    int insert(Role role);

    int update(Role role);

    List<Role> findListByEntity(Role role);

    List<Role> queryListPage(RolePageInfo rolePageInfo);

    Long queryListPageCount(RolePageInfo rolePageInfo);

    int deleteById(Long id);

    List<Role> findListByRoleIds(@org.apache.ibatis.annotations.Param("roleIds") String[] roleIds);
    RoleVO findVOById(Long id);
    Role findByRoleId(@org.apache.ibatis.annotations.Param("roleId") String roleId);
}
