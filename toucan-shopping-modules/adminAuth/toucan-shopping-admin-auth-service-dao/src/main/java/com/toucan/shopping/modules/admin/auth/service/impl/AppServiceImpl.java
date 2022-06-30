package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.mapper.AppMapper;
import com.toucan.shopping.modules.admin.auth.page.AppPageInfo;
import com.toucan.shopping.modules.admin.auth.service.AppService;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.common.page.PageInfo;
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
    public List<App> findListByEntity(App app) {
        return appMapper.findListByEntity(app);
    }

    @Override
    public App findByAppCode(String appCode) {
        return appMapper.findByAppCode(appCode);
    }

    @Override
    public int save(App user) {
        return appMapper.insert(user);
    }

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
    public PageInfo<AppVO> queryListPage(AppPageInfo appPageInfo) {
        appPageInfo.setStart(appPageInfo.getPage()*appPageInfo.getLimit()-appPageInfo.getLimit());
        AppPageInfo pageInfo = new AppPageInfo();
        pageInfo.setList(appMapper.queryListPage(appPageInfo));
        pageInfo.setTotal(appMapper.queryListPageCount(appPageInfo));
        pageInfo.setLimit(appPageInfo.getLimit());
        pageInfo.setPage(appPageInfo.getPage());
        pageInfo.setSize(appPageInfo.getSize());
        return pageInfo;
    }

    @Override
    public int deleteById(Long id) {
        return appMapper.deleteById(id);
    }

}
