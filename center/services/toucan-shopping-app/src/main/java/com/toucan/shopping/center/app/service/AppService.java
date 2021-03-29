package com.toucan.shopping.center.app.service;


import com.toucan.shopping.center.app.entity.App;
import com.toucan.shopping.center.app.entity.AppPageInfo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AppService {

    List<App> findListByEntity(App app);

    int save(App app);

    int update(App app);

    boolean exists(String name);

    PageInfo<App> queryListPage(AppPageInfo appPageInfo);

    int deleteById(Integer id);
}
