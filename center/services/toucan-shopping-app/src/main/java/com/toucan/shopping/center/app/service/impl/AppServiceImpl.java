package com.toucan.shopping.center.app.service.impl;

import com.toucan.shopping.center.app.entity.App;
import com.toucan.shopping.center.app.entity.AppPageInfo;
import com.toucan.shopping.center.app.mapper.AppMapper;
import com.toucan.shopping.center.app.service.AppService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
        PageHelper.startPage(appPageInfo.getPage(), appPageInfo.getSize());
        List<App> apps = appMapper.queryListPage(appPageInfo);
        PageInfo<App> pageInfo = new PageInfo<>(apps);
        return pageInfo;
    }

    @Transactional
    @Override
    public int deleteById(Integer id) {
        return appMapper.deleteById(id);
    }

}
