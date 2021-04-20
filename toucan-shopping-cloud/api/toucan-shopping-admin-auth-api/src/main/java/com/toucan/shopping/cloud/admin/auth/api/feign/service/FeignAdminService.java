package com.toucan.shopping.cloud.admin.auth.api.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.feign.fallback.FeignAdminServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/admin",fallbackFactory = FeignAdminServiceFallbackFactory.class)
public interface FeignAdminService {

    @PostMapping("/login")
    ResultObjectVO login(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);

    @PostMapping("/query/login/token")
    ResultObjectVO queryLoginToken(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);

    @PostMapping("/is/online")
    ResultObjectVO isOnline(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 保存
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",method = RequestMethod.POST)
    ResultObjectVO save(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    /**
     * 列表分页
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO list(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


}
