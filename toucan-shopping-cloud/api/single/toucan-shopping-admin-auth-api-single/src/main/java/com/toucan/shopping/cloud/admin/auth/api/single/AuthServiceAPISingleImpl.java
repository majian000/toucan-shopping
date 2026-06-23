package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.AuthServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceAPISingleImpl implements AuthServiceAPI {


    @Override
    public ResultObjectVO verify(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO verifyLoginAndUrl(String signHeader, RequestJsonVO requestVo) {
        return null;
    }
}
