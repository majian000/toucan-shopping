package com.toucan.shopping.admin.auth.service;


import com.github.pagehelper.PageInfo;
import com.toucan.shopping.admin.auth.entity.App;
import com.toucan.shopping.admin.auth.page.AppPageInfo;

import java.util.List;

public interface AppService {

    List<App> findListByEntity(App app);

    int save(App app);

    int update(App app);

    boolean exists(String name);

    PageInfo<App> queryListPage(AppPageInfo appPageInfo);

    int deleteById(Long id);
}
