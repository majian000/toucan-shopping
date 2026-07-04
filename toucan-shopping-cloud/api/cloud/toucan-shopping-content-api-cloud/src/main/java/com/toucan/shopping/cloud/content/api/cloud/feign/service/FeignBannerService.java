package com.toucan.shopping.cloud.content.api.cloud.feign.service;

import com.toucan.shopping.cloud.content.api.BannerServiceAPI;
import com.toucan.shopping.cloud.content.api.cloud.feign.fallback.FeignBannerServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-content-proxy/banner", fallbackFactory = FeignBannerServiceFallbackFactory.class)
public interface FeignBannerService extends BannerServiceAPI {

    @Override
    @RequestMapping(value = "/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);


    @Override
    @RequestMapping(value = "/query/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryList(@RequestBody RequestJsonVO requestJsonVO);


    @Override
    @RequestMapping(value = "/flush/index/cache", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO flushWebIndexCache(@RequestBody RequestJsonVO requestVo);


    @Override
    @RequestMapping(value = "/query/index/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryIndexList(@RequestBody RequestJsonVO requestJsonVO);


    @Override
    @RequestMapping(value = "/clear/index/cache", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO clearWebIndexCache(@RequestBody RequestJsonVO requestVo);


    @Override
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    ResultObjectVO save(@RequestBody RequestJsonVO requestVo);


    @Override
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    ResultObjectVO update(@RequestBody RequestJsonVO requestVo);


    @Override
    @RequestMapping(value = "/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO findById(@RequestBody RequestJsonVO requestVo);


    @Override
    @RequestMapping(value = "/delete/id", method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo);


    @Override
    @RequestMapping(value = "/delete/ids", method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo);


}
