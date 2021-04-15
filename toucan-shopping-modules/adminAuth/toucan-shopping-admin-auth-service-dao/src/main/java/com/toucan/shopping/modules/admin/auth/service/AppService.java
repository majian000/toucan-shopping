package com.toucan.shopping.modules.admin.auth.service;


import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.page.AppPageInfo;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.util.List;

public interface AppService {

    List<App> findListByEntity(App app);

    int save(App app);

    int update(App app);

    boolean exists(String name);

    PageInfo<App> queryListPage(AppPageInfo appPageInfo);

    int deleteById(Long id);
}
