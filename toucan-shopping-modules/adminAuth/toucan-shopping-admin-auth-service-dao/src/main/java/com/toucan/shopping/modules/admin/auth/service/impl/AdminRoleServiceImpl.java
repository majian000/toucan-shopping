package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.mapper.AdminAppMapper;
import com.toucan.shopping.modules.admin.auth.mapper.AdminRoleMapper;
import com.toucan.shopping.modules.admin.auth.page.AdminRolePageInfo;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.service.AdminRoleService;
import com.toucan.shopping.modules.admin.auth.vo.AdminRoleVO;
import com.toucan.shopping.modules.common.page.PageInfo;
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
    public int saves(AdminRole[] adminRole) {
        return adminRoleMapper.inserts(adminRole);
    }
    @Override
    public int deleteByRoleId(String roleId) {
        return adminRoleMapper.deleteByRoleId(roleId);
    }

    @Override
    public int deleteByAdminId(String adminId) {
        return adminRoleMapper.deleteByAdminId(adminId);
    }

    @Override
    public int deleteByAdminIdAndAppCodes(String adminId, String[] appCodes) {
        return adminRoleMapper.deleteByAdminIdAndAppCodes(adminId,appCodes);
    }

    @Override
    public List<AdminRole> findListByAdminId(String adminId) {
        return adminRoleMapper.findListByAdminId(adminId);
    }

    @Override
    public List<AdminRole> listByAdminIdAndAppCode(String adminId, String appCode) {
        return adminRoleMapper.listByAdminIdAndAppCode(adminId,appCode);
    }

    @Override
    public PageInfo<AdminRole> queryListPage(AdminRolePageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<AdminRole> pageInfo = new PageInfo();
        pageInfo.setList(adminRoleMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(adminRoleMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }
}
