package com.toucan.shopping.admin.auth.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.toucan.shopping.admin.auth.entity.App;
import com.toucan.shopping.admin.auth.mapper.AppMapper;
import com.toucan.shopping.admin.auth.page.AppPageInfo;
import com.toucan.shopping.admin.auth.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private AppMapper appMapper;

    @Override
    public List<App> findListByEntity(App user) {
        return appMapper.findListByEntity(user);
    }

    @Transactional
    @Override
    public int save(App user) {
        return appMapper.insert(user);
    }

    @Transactional
    @Override
    public int update(App app) {
        return appMapper.update(app);
    }

    @Override
    public boolean exists(String name) {
        App app = new App();
        app.setName(name);
        app.setDeleteStatus((short)0);
        List<App> users = appMapper.findListByEntity(app);
        if(!CollectionUtils.isEmpty(users))
        {
            return true;
        }
        return false;
    }

    @Override
    public PageInfo<App> queryListPage(AppPageInfo appPageInfo) {
        //PageHelper.startPage(appPageInfo.getPage(), appPageInfo.getSize());
        appPageInfo.setSize(appPageInfo.getLimit());
        appPageInfo.setLimit(appPageInfo.getPage()*appPageInfo.getSize()-appPageInfo.getSize());
        List<App> apps = appMapper.queryListPage(appPageInfo);
        PageInfo<App> pageInfo = new PageInfo<>(apps);
        pageInfo.setTotal(16);
        return pageInfo;
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        return appMapper.deleteById(id);
    }

}
