package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.AdminLoginHistoryServiceAPI;
import com.toucan.shopping.modules.admin.auth.business.service.AdminLoginHistoryBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminLoginHistoryServiceAPISingleImpl implements AdminLoginHistoryServiceAPI {

    @Autowired
    private AdminLoginHistoryBusinessService adminLoginHistoryBusinessService;

    @Override
    public ResultObjectVO listPage(RequestJsonVO requestVo) {
        return adminLoginHistoryBusinessService.listPage(requestVo);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return adminLoginHistoryBusinessService.findById(requestVo);
    }
}
