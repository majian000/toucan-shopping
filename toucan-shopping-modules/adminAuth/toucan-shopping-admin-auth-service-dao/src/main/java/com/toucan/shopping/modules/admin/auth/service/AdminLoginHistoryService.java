package com.toucan.shopping.modules.admin.auth.service;

import com.toucan.shopping.modules.admin.auth.entity.AdminLoginHistory;
import com.toucan.shopping.modules.admin.auth.vo.AdminLoginHistoryVO;

/**
 * 登录历史服务
 */
public interface AdminLoginHistoryService {

    int save(AdminLoginHistory adminLoginHistory);
}
