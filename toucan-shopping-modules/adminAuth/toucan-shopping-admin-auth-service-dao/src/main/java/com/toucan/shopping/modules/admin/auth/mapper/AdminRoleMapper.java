package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AdminRoleMapper {

    int insert(AdminRole AdminRole);

    List<AdminRole> findListByEntity(AdminRole adminRole);

    int deleteByRoleId(String roleId);

    List<AdminRole> findListByAdminId(String adminId);

    int deleteByAdminId(String adminId);

    int deleteByAdminIdAndAppCodes(String adminId,String[] appCodes);

}
