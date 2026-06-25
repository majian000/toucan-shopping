package com.toucan.shopping.cloud.content.api.cloud.feign.service;

import com.toucan.shopping.cloud.content.api.cloud.feign.fallback.FeignBannerServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-content-proxy/banner", fallbackFactory = FeignBannerServiceFallbackFactory.class)
public interface FeignBannerService extends com.toucan.shopping.cloud.content.api.feign.service.FeignBannerService {

    @Override
    @RequestMapping(value = "/query/list/page", produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/query/list", produces = "application/json;charset=UTF-8")
    ResultObjectVO queryList(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/flush/index/cache", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO flushWebIndexCache(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestVo);

    @Override
    @RequestMapping(value = "/query/index/list", produces = "application/json;charset=UTF-8")
    ResultObjectVO queryIndexList(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/clear/index/cache", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO clearWebIndexCache(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestVo);

    @Override
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    ResultObjectVO save(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);

    @Override
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    ResultObjectVO update(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);

    @Override
    @RequestMapping(value = "/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO findById(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);

    @Override
    @RequestMapping(value = "/delete/id", method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);

    @Override
    @RequestMapping(value = "/delete/ids", method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);

}
