package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.entity.AdminLoginHistory;
import com.toucan.shopping.modules.admin.auth.mapper.AdminLoginHistoryMapper;
import com.toucan.shopping.modules.admin.auth.page.AdminLoginHistoryPageInfo;
import com.toucan.shopping.modules.admin.auth.service.AdminLoginHistoryService;
import com.toucan.shopping.modules.admin.auth.vo.AdminLoginHistoryVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminLoginHistoryServiceImpl implements AdminLoginHistoryService {


    @Autowired
    private AdminLoginHistoryMapper adminLoginHistoryMapper;

    @Override
    public int save(AdminLoginHistory adminLoginHistory) {
        return adminLoginHistoryMapper.insert(adminLoginHistory);
    }

    @Override
    public PageInfo<AdminLoginHistoryVO> queryListPage(AdminLoginHistoryPageInfo pageInfo) {
        pageInfo.setStart(pageInfo.getPage()*pageInfo.getLimit()-pageInfo.getLimit());
        PageInfo<AdminLoginHistoryVO> result = new PageInfo<>();
        result.setList(adminLoginHistoryMapper.queryListPage(pageInfo));
        result.setTotal(adminLoginHistoryMapper.queryListPageCount(pageInfo));
        result.setLimit(pageInfo.getLimit());
        result.setPage(pageInfo.getPage());
        result.setSize(pageInfo.getSize());
        return result;
    }

    @Override
    public List<AdminLoginHistory> findListByEntity(AdminLoginHistory entity) {
        return adminLoginHistoryMapper.findListByEntity(entity);
    }
}
