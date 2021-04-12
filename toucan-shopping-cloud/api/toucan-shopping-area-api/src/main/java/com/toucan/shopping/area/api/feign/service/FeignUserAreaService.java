package com.toucan.shopping.area.api.feign.service;

import com.toucan.shopping.area.api.feign.fallback.FeignAdminAreaServiceFallbackFactory;
import com.toucan.shopping.area.api.feign.fallback.FeignUserAreaServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-area-proxy/area/user",fallbackFactory = FeignUserAreaServiceFallbackFactory.class)
public interface FeignUserAreaService {

    @RequestMapping(value="/query/all",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryAll(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

}
