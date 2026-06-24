package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.AuthServiceAPI;
import com.toucan.shopping.modules.admin.auth.business.service.AuthBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceAPISingleImpl implements AuthServiceAPI {

    @Autowired
    private AuthBusinessService authBusinessService;

    @Override
    public ResultObjectVO verify(String signHeader, RequestJsonVO requestVo) {
        return authBusinessService.verify(requestVo);
    }

    @Override
    public ResultObjectVO verifyLoginAndUrl(String signHeader, RequestJsonVO requestVo) {
        return authBusinessService.verifyLoginAndUrl(requestVo);
    }
}
