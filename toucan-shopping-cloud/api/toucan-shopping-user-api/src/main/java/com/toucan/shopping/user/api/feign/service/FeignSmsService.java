package com.toucan.shopping.user.api.feign.service;

import com.toucan.shopping.user.api.feign.fallback.FeignSmsServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 短信服务
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-user-proxy/sms",fallbackFactory = FeignSmsServiceFallbackFactory.class)
public interface FeignSmsService {

    /**
     * 发送短信验证码
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/send",produces = "application/json;charset=UTF-8")
    public ResultObjectVO send(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

}
