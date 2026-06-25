package com.toucan.shopping.cloud.content.api.cloud.feign.service;

import com.toucan.shopping.cloud.content.api.cloud.feign.fallback.FeignBannerAreaServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-content-proxy/bannerArea", fallbackFactory = FeignBannerAreaServiceFallbackFactory.class)
public interface FeignBannerAreaService extends com.toucan.shopping.cloud.content.api.feign.service.FeignBannerAreaService {

    @Override
    @RequestMapping(value = "/query/list", method = RequestMethod.POST)
    ResultObjectVO queryBannerAreaList(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

}
