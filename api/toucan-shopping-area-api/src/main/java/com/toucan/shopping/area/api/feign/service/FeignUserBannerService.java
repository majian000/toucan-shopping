package com.toucan.shopping.area.api.feign.service;

import com.toucan.shopping.area.api.feign.fallback.FeignUserAreaServiceFallbackFactory;
import com.toucan.shopping.area.api.feign.fallback.FeignUserBannerServiceFallbackFactory;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-area-proxy/banner/user",fallbackFactory = FeignUserBannerServiceFallbackFactory.class)
public interface FeignUserBannerService {


    @RequestMapping(value="/query/list",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryList(@RequestHeader(value = "bbs-sign-header", defaultValue = "-1") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

}
