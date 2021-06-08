package com.toucan.shopping.cloud.area.api.feign.service;

import com.toucan.shopping.cloud.area.api.feign.fallback.FeignBannerServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-area-proxy/banner/user",fallbackFactory = FeignBannerServiceFallbackFactory.class)
public interface FeignBannerService {


    @RequestMapping(value="/query/list",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryList(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 保存
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",method = RequestMethod.POST)
    ResultObjectVO save(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);



}
