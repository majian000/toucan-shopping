package com.toucan.shopping.center.admin.auth.api.feign.service;

import com.toucan.shopping.center.admin.auth.api.feign.fallback.FeignAdminServiceFallbackFactory;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-center-proxy/toucan-shopping-admin-auth-center/adminApp",fallbackFactory = FeignAdminServiceFallbackFactory.class)
public interface FeignAdminAppService {

    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestVo);


    @RequestMapping(value="/queryListByEntity",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListByEntity(@RequestBody RequestJsonVO requestVo);


    @RequestMapping(value="/deleteByAppCode",produces = "application/json;charset=UTF-8")
    ResultObjectVO deleteByAppCode(@RequestBody RequestJsonVO requestVo);


}
