package com.toucan.shopping.cloud.admin.auth.api.cloud.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.AuthServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.cloud.feign.fallback.FeignAuthServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/auth",fallbackFactory = FeignAuthServiceFallbackFactory.class)
public interface FeignAuthService extends AuthServiceAPI {



    @RequestMapping(value="/verify",produces = "application/json;charset=UTF-8")
    ResultObjectVO verify(@RequestBody RequestJsonVO requestVo);


    /**
     * 校验权限 并验证是否登录
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/verifyLoginAndUrl",produces = "application/json;charset=UTF-8")
    ResultObjectVO verifyLoginAndUrl(@RequestBody RequestJsonVO requestVo);

}
