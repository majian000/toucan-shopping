package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.entity.AdminInfo;
import com.toucan.shopping.modules.admin.auth.mapper.AdminInfoMapper;
import com.toucan.shopping.modules.admin.auth.service.AdminInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminInfoServiceImpl implements AdminInfoService {

    @Autowired
    private AdminInfoMapper adminInfoMapper;

    @Override
    public int save(AdminInfo adminInfo) {
        return adminInfoMapper.insert(adminInfo);
    }

    @Override
    public int update(AdminInfo adminInfo) {
        return adminInfoMapper.update(adminInfo);
    }

    @Override
    public AdminInfo findByAdminId(String adminId) {
        return adminInfoMapper.findByAdminId(adminId);
    }
}
