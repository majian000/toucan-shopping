package com.toucan.shopping.cloud.admin.auth.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface AuthServiceAPI {



    ResultObjectVO verify(RequestJsonVO requestVo);


    /**
     * 校验权限 并验证是否登录
     * -1登录超时 -2权限校验失败 1成功
     * @param requestVo
     * @return
     */
    ResultObjectVO verifyLoginAndUrl(RequestJsonVO requestVo);

}
