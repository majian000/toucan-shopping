package com.toucan.shopping.cloud.admin.auth.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface AuthServiceAPI {



    ResultObjectVO verify( String signHeader, RequestJsonVO requestVo);


    /**
     * 校验权限 并验证是否登录
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO verifyLoginAndUrl( String signHeader, RequestJsonVO requestVo);

}
