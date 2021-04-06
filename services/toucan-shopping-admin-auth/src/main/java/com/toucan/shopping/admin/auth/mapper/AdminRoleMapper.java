package com.toucan.shopping.admin.auth.mapper;

import com.toucan.shopping.admin.auth.entity.AdminRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AdminRoleMapper {

    int insert(AdminRole AdminRole);

    List<AdminRole> findListByEntity(AdminRole adminRole);

    int deleteByRoleId(Long roleId);


}
