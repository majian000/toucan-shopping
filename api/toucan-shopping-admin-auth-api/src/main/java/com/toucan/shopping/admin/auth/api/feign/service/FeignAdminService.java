package com.toucan.shopping.admin.auth.api.feign.service;

import com.toucan.shopping.admin.auth.api.feign.fallback.FeignAdminServiceFallbackFactory;
import com.toucan.shopping.admin.auth.api.feign.fallback.FeignAdminServiceFallbackFactory;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/admin",fallbackFactory = FeignAdminServiceFallbackFactory.class)
public interface FeignAdminService {

    @PostMapping("/login")
    ResultObjectVO login(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);

    @PostMapping("/query/login/token")
    ResultObjectVO queryLoginToken(@RequestBody RequestJsonVO requestVo);

    @PostMapping("/is/online")
    ResultObjectVO isOnline(@RequestBody RequestJsonVO requestVo);


}
