package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.AdminRoleServiceAPI;
import com.toucan.shopping.modules.admin.auth.controller.admin.AdminRoleController;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminRoleServiceAPISingleImpl implements AdminRoleServiceAPI {

    @Autowired
    private AdminRoleController adminRoleController;

    @Override
    public ResultObjectVO saveRoles(String signHeader, RequestJsonVO requestJsonVO) {
        return null;
    }

    @Override
    public ResultObjectVO queryListByEntity(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO list(String signHeader, RequestJsonVO requestVo) {
        return null;
    }
}
