package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.AdminRoleServiceAPI;
import com.toucan.shopping.modules.admin.auth.business.service.AdminRoleBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminRoleServiceAPISingleImpl implements AdminRoleServiceAPI {

    @Autowired
    private AdminRoleBusinessService adminRoleBusinessService;

    @Override
    public ResultObjectVO saveRoles(RequestJsonVO requestJsonVO) {
        return adminRoleBusinessService.saveRoles(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListByEntity(RequestJsonVO requestVo) {
        return adminRoleBusinessService.queryListByEntity(requestVo);
    }

    @Override
    public ResultObjectVO list(RequestJsonVO requestVo) {
        return adminRoleBusinessService.list(requestVo);
    }
}
