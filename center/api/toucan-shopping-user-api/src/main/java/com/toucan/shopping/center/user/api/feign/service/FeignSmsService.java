package com.toucan.shopping.center.user.api.feign.service;

import com.toucan.shopping.center.user.api.feign.fallback.FeignSmsServiceFallbackFactory;
import com.toucan.shopping.center.user.export.vo.UserSmsVO;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
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
    public ResultObjectVO send(@RequestHeader("bbs-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

}
