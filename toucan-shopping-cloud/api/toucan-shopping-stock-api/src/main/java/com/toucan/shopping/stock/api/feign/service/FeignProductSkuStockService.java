package com.toucan.shopping.stock.api.feign.service;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.stock.api.feign.fallback.FeignProductSkuStockServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-stock-proxy/productSkuStock",fallbackFactory = FeignProductSkuStockServiceFallbackFactory.class)
public interface FeignProductSkuStockService {


    @RequestMapping(value = "/inventoryReduction",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO inventoryReduction(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value = "/restore/stock",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO restoreStock(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/restore/cache/stock",produces = "application/json;charset=UTF-8")
    ResultObjectVO restoreCacheStock(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/query/sku/uuids",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryBySkuUuidList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/query/stock/cache/sku/uuids",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryStockCacheBySkuUuidList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(method= RequestMethod.POST,value="/deduct/cache/stock",produces = "application/json;charset=UTF-8")
    ResultObjectVO deductCacheStock(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

}
