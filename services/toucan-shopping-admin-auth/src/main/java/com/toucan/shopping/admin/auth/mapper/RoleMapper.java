package com.toucan.shopping.admin.auth.mapper;

import com.toucan.shopping.admin.auth.entity.Role;
import com.toucan.shopping.admin.auth.page.RolePageInfo;
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
}
