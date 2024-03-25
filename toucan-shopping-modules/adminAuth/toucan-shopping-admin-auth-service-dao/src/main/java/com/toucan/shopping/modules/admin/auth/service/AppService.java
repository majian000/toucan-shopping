package com.toucan.shopping.modules.admin.auth.service;


import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.page.AppPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.util.List;

public interface AppService {

    List<App> findListByEntity(App app);

    boolean existsByCode(String code);

    App findByAppCode(String appCode);

    List<App> queryListByCodesIngoreDelete(List<String> codes);

    int save(App app);

    int update(App app);

    boolean exists(String name);

    PageInfo<AppVO> queryListPage(AppPageInfo appPageInfo);

    int deleteById(Long id);

    int deleteById(Long id,String adminId);

    AppVO findByCodeIngoreDelete(String code);
}
