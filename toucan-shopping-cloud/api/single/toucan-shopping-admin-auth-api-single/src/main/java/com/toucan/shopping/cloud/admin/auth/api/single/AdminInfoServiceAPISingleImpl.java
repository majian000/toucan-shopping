package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.AdminInfoServiceAPI;
import com.toucan.shopping.modules.admin.auth.business.service.AdminInfoBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminInfoServiceAPISingleImpl implements AdminInfoServiceAPI {

    @Autowired
    private AdminInfoBusinessService adminInfoBusinessService;

    @Override
    public ResultObjectVO saveOrUpdate(RequestJsonVO requestVo) {
        return adminInfoBusinessService.saveOrUpdate(requestVo);
    }

    @Override
    public ResultObjectVO findByAdminId(RequestJsonVO requestVo) {
        return adminInfoBusinessService.findByAdminId(requestVo);
    }
}
