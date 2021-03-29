package com.toucan.shopping.center.admin.auth.service.impl;

import com.toucan.shopping.center.admin.auth.entity.Admin;
import com.toucan.shopping.center.admin.auth.mapper.AdminMapper;
import com.toucan.shopping.center.admin.auth.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public List<Admin> findListByEntity(Admin admin) {
        return adminMapper.findListByEntity(admin);
    }

    @Transactional
    @Override
    public int save(Admin admin) {
        return adminMapper.insert(admin);
    }

    @Override
    public boolean exists(String username) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setDeleteStatus((short)0);
        List<Admin> users = adminMapper.findListByEntity(admin);
        if(!CollectionUtils.isEmpty(users))
        {
            return true;
        }
        return false;
    }

}
