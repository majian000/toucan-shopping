package com.toucan.shopping.admin.auth.service.impl;

import com.toucan.shopping.admin.auth.entity.AdminApp;
import com.toucan.shopping.admin.auth.entity.AdminRole;
import com.toucan.shopping.admin.auth.mapper.AdminAppMapper;
import com.toucan.shopping.admin.auth.mapper.AdminRoleMapper;
import com.toucan.shopping.admin.auth.service.AdminAppService;
import com.toucan.shopping.admin.auth.service.AdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminRoleServiceImpl implements AdminRoleService {

    @Autowired
    private AdminRoleMapper adminRoleMapper;


    @Override
    public List<AdminRole> findListByEntity(AdminRole adminRole) {
        return adminRoleMapper.findListByEntity(adminRole);
    }

    @Override
    public int save(AdminRole adminRole) {
        return adminRoleMapper.insert(adminRole);
    }

    @Override
    public int deleteByRoleId(Long roleId) {
        return adminRoleMapper.deleteByRoleId(roleId);
    }
}
