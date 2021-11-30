package com.toucan.shopping.cloud.admin.auth.api.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.feign.fallback.FeignAdminAppServiceFallbackFactory;
import com.toucan.shopping.cloud.admin.auth.api.feign.fallback.FeignAuthServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/auth",fallbackFactory = FeignAuthServiceFallbackFactory.class)
public interface FeignAuthService {



    @RequestMapping(value="/verify",produces = "application/json;charset=UTF-8")
    ResultObjectVO verify(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 校验权限 并验证是否登录
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/verifyLoginAndUrl",produces = "application/json;charset=UTF-8")
    ResultObjectVO verifyLoginAndUrl(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);

}
