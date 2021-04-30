package com.toucan.shopping.cloud.admin.auth.api.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.feign.fallback.FeignAdminAppServiceFallbackFactory;
import com.toucan.shopping.cloud.admin.auth.api.feign.fallback.FeignAdminOrgnazitionServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/adminOrgnazition",fallbackFactory = FeignAdminOrgnazitionServiceFallbackFactory.class)
public interface FeignAdminOrgnazitionService {

    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    @RequestMapping(value="/queryListByEntity",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListByEntity(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    @RequestMapping(value="/deleteByAppCode",produces = "application/json;charset=UTF-8")
    ResultObjectVO deleteByAppCode(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    @RequestMapping(value="/queryAppListByAdminId",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryAppListByAdminId(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);



}
