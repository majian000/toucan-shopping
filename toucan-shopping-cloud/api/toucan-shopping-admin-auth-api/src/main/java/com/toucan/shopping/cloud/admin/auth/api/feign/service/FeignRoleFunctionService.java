package com.toucan.shopping.cloud.admin.auth.api.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.feign.fallback.FeignRoleFunctionServiceFallbackFactory;
import com.toucan.shopping.cloud.admin.auth.api.feign.fallback.FeignRoleServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/roleFunction",fallbackFactory = FeignRoleFunctionServiceFallbackFactory.class)
public interface FeignRoleFunctionService {


    @RequestMapping(value = "/save/functions",method = RequestMethod.POST)
    public ResultObjectVO saveFunctions(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value = "/query/list",method = RequestMethod.POST)
    ResultObjectVO queryRoleFunctionList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

}
