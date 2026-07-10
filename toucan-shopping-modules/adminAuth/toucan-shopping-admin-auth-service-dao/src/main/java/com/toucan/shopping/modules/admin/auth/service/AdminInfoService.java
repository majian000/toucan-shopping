package com.toucan.shopping.modules.admin.auth.service;

import com.toucan.shopping.modules.admin.auth.entity.AdminInfo;

/**
 * 管理员信息服务
 */
public interface AdminInfoService {

    int save(AdminInfo adminInfo);

    int update(AdminInfo adminInfo);

    AdminInfo findByAdminId(String adminId);
}
