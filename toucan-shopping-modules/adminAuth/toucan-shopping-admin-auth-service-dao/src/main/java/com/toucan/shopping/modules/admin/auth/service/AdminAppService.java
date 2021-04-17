package com.toucan.shopping.modules.admin.auth.service;




import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;

import java.util.List;

public interface AdminAppService {

    List<AdminApp> findListByEntity(AdminApp adminApp);

    int save(AdminApp adminApp);

    int deleteByAppCode(String appCode);

    List<AdminAppVO> findAppListByAdminAppEntity(AdminApp query);
}
