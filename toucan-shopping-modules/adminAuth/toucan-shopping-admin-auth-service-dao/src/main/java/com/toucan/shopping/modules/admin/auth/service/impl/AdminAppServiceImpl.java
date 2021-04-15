package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.mapper.AdminAppMapper;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminAppServiceImpl implements AdminAppService {

    @Autowired
    private AdminAppMapper adminAppMapper;

    @Override
    public List<AdminApp> findListByEntity(AdminApp adminApp) {
        return adminAppMapper.findListByEntity(adminApp);
    }

    @Transactional
    @Override
    public int save(AdminApp adminApp) {
        return adminAppMapper.insert(adminApp);
    }

    @Transactional
    @Override
    public int deleteByAppCode(String appCode) {
        return adminAppMapper.deleteByAppCode(appCode);
    }


}
