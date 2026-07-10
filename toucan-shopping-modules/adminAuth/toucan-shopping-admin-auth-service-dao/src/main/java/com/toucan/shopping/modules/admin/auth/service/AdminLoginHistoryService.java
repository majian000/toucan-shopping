package com.toucan.shopping.modules.admin.auth.service;

import com.toucan.shopping.modules.admin.auth.entity.AdminLoginHistory;
import com.toucan.shopping.modules.admin.auth.page.AdminLoginHistoryPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.AdminLoginHistoryVO;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.util.List;

/**
 * 登录历史服务
 */
public interface AdminLoginHistoryService {

    int save(AdminLoginHistory adminLoginHistory);

    /**
     * 查询列表分页
     * @param pageInfo
     * @return
     */
    PageInfo<AdminLoginHistoryVO> queryListPage(AdminLoginHistoryPageInfo pageInfo);

    /**
     * 根据实体查询列表
     * @param entity
     * @return
     */
    List<AdminLoginHistory> findListByEntity(AdminLoginHistory entity);
}
