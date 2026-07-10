package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.AdminInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员信息Mapper
 */
@Mapper
public interface AdminInfoMapper {

    int insert(AdminInfo adminInfo);

    int update(AdminInfo adminInfo);

    AdminInfo findByAdminId(String adminId);
}
