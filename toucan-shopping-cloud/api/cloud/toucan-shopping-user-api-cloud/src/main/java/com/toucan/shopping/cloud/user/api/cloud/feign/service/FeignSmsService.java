package com.toucan.shopping.cloud.user.api.cloud.feign.service;

import com.toucan.shopping.cloud.user.api.SmsServiceAPI;
import com.toucan.shopping.cloud.user.api.cloud.feign.fallback.FeignSmsServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 短信服务
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-user-proxy/sms",fallbackFactory = FeignSmsServiceFallbackFactory.class)
public interface FeignSmsService extends SmsServiceAPI {

    /**
     * 发送短信验证码
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/send", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO send(@RequestBody RequestJsonVO requestJsonVO);

}
