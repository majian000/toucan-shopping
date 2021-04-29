package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.entity.AdminOrgnazition;
import com.toucan.shopping.modules.admin.auth.mapper.AdminOrgnazitionMapper;
import com.toucan.shopping.modules.admin.auth.service.AdminOrgnazitionService;
import com.toucan.shopping.modules.admin.auth.vo.AdminOrgnazitionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminOrgnazitionServiceImpl implements AdminOrgnazitionService {

    @Autowired
    private AdminOrgnazitionMapper adminOrgnazitionMapper;

    @Override
    public List<AdminOrgnazition> findListByEntity(AdminOrgnazition AdminOrgnazition) {
        return adminOrgnazitionMapper.findListByEntity(AdminOrgnazition);
    }

    @Override
    public int save(AdminOrgnazition AdminOrgnazition) {
        return adminOrgnazitionMapper.insert(AdminOrgnazition);
    }

    @Override
    public int deleteByAppCode(String appCode) {
        return adminOrgnazitionMapper.deleteByAppCode(appCode);
    }

    @Override
    public int deleteByAdminId(String adminId) {
        return adminOrgnazitionMapper.deleteByAdminId(adminId);
    }

    @Override
    public int deleteByOrgnazitionId(String orgnazitionId) {
        return adminOrgnazitionMapper.deleteByOrgnazitionId(orgnazitionId);
    }


    @Override
    public int deleteByAdminIdAndAppCode(String adminId, String appCode) {
        return adminOrgnazitionMapper.deleteByAdminIdAndAppCode(adminId,appCode);
    }


}
