package com.toucan.shopping.cloud.area.api.feign.service;

import com.toucan.shopping.cloud.area.api.feign.fallback.FeignBannerAreaServiceFallbackFactory;
import com.toucan.shopping.cloud.area.api.feign.fallback.FeignBannerServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-area-proxy/bannerArea",fallbackFactory = FeignBannerAreaServiceFallbackFactory.class)
public interface FeignBannerAreaService {


    @RequestMapping(value = "/query/list",method = RequestMethod.POST)
    public ResultObjectVO queryBannerAreaList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);




}
