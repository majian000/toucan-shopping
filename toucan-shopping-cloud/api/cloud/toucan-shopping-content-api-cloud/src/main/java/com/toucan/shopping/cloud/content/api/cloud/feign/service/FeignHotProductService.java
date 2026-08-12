package com.toucan.shopping.cloud.content.api.cloud.feign.service;

import com.toucan.shopping.cloud.content.api.HotProductServiceAPI;
import com.toucan.shopping.cloud.content.api.cloud.feign.fallback.FeignHotProductServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-content-proxy/hot/product", fallbackFactory = FeignHotProductServiceFallbackFactory.class)
public interface FeignHotProductService extends HotProductServiceAPI {

    @Override
    @RequestMapping(value = "/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO findById(@RequestBody RequestJsonVO requestVo);

    @Override
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/pc/index/hot/products", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO queryPcIndexHotProducts(@RequestBody RequestJsonVO requestVo);

}
