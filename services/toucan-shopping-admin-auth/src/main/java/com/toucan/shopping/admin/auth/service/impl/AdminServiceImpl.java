package com.toucan.shopping.admin.auth.service.impl;

import com.toucan.shopping.admin.auth.page.AdminPageInfo;
import com.toucan.shopping.admin.auth.service.AdminService;
import com.toucan.shopping.admin.auth.entity.Admin;
import com.toucan.shopping.admin.auth.mapper.AdminMapper;
import com.toucan.shopping.admin.auth.service.AdminService;
import com.toucan.shopping.admin.auth.vo.AdminVO;
import com.toucan.shopping.common.page.PageInfo;
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

    @Override
    public PageInfo<AdminVO> queryListPage(AdminPageInfo appPageInfo) {
        List<AdminVO> admins = adminMapper.queryListPage(appPageInfo);
        PageInfo<AdminVO> pageInfo = new PageInfo();
        pageInfo.setList(admins);
        return pageInfo;
    }

}
