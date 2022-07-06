package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.entity.AdminLoginHistory;
import com.toucan.shopping.modules.admin.auth.mapper.AdminLoginHistoryMapper;
import com.toucan.shopping.modules.admin.auth.service.AdminLoginHistoryService;
import com.toucan.shopping.modules.admin.auth.vo.AdminLoginHistoryVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AdminLoginHistoryServiceImpl implements AdminLoginHistoryService {


    @Autowired
    private AdminLoginHistoryMapper adminLoginHistoryMapper;

    @Override
    public int save(AdminLoginHistory adminLoginHistory) {
        return adminLoginHistoryMapper.insert(adminLoginHistory);
    }
}
