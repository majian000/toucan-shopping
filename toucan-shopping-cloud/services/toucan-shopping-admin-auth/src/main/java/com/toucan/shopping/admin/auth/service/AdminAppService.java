package com.toucan.shopping.admin.auth.service;




import com.toucan.shopping.admin.auth.entity.AdminApp;

import java.util.List;

public interface AdminAppService {

    List<AdminApp> findListByEntity(AdminApp adminApp);

    int save(AdminApp adminApp);

    int deleteByAppCode(String appCode);

}
