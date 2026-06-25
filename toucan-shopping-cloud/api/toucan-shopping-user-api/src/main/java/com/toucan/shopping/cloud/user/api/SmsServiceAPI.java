package com.toucan.shopping.cloud.user.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

/**
 * 短信服务
 */
public interface SmsServiceAPI {

    /**
     * 发送短信验证码
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO send(RequestJsonVO requestJsonVO);

}
