package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.AdminRoleServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.stereotype.Service;

@Service
public class AdminRoleServiceAPISingleImpl implements AdminRoleServiceAPI {


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
