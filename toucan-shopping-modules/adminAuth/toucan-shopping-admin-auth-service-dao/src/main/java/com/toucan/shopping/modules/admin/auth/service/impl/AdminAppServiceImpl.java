package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.page.AdminAppPageInfo;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.mapper.AdminAppMapper;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.common.page.PageInfo;
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

    @Override
    public int save(AdminApp adminApp) {
        return adminAppMapper.insert(adminApp);
    }

    @Override
    public int deleteByAppCode(String appCode) {
        return adminAppMapper.deleteByAppCode(appCode);
    }

    @Override
    public int deleteByAdminId(String adminId) {
        return adminAppMapper.deleteByAdminId(adminId);
    }

    @Override
    public List<AdminAppVO> findAppListByAdminAppEntity(AdminApp query) {
        return adminAppMapper.findAppListByAdminAppEntity(query);
    }

    @Override
    public int deleteByAdminIdAndAppCode(String adminId, String appCode) {
        return adminAppMapper.deleteByAdminIdAndAppCode(adminId,appCode);
    }

    @Override
    public int updateLoginStatus(String adminId, String appCode, Short loginStatus) {
        return adminAppMapper.updateLoginStatus(adminId,appCode,loginStatus);
    }


    @Override
    public PageInfo<AdminAppVO> queryListPage(AdminAppPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<AdminAppVO> pageInfo = new PageInfo();
        pageInfo.setList(adminAppMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(adminAppMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public PageInfo<AdminAppVO> queryLoginListPage(AdminAppPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<AdminAppVO> pageInfo = new PageInfo();
        pageInfo.setList(adminAppMapper.queryLoginListPage(queryPageInfo));
        pageInfo.setTotal(adminAppMapper.queryLoginListPageCount(queryPageInfo));
        return pageInfo;
    }

}
