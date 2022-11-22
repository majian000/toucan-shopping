package com.toucan.shopping.cloud.stock.api.feign.service;

import com.toucan.shopping.cloud.stock.api.feign.fallback.FeignProductSkuStockLockServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-stock-proxy/productSkuStockLock",fallbackFactory = FeignProductSkuStockLockServiceFallbackFactory.class)
public interface FeignProductSkuStockLockService {


    /**
     * 锁定库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/lock/stock",produces = "application/json;charset=UTF-8")
    ResultObjectVO lockStock(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 根据SKUID查询锁定库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/lock/stock/num",produces = "application/json;charset=UTF-8")
    ResultObjectVO findLockStockNumByProductSkuIds(@RequestBody RequestJsonVO requestJsonVO);

//    @RequestMapping(value = "/inventoryReduction",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
//    ResultObjectVO inventoryReduction(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);
//
//    @RequestMapping(value = "/restore/stock",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
//    ResultObjectVO restoreStock(@RequestBody RequestJsonVO requestJsonVO);
//
//    @RequestMapping(value="/restore/cache/stock",produces = "application/json;charset=UTF-8")
//    ResultObjectVO restoreCacheStock(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);
//
//    @RequestMapping(value="/query/sku/uuids",produces = "application/json;charset=UTF-8")
//    ResultObjectVO queryBySkuUuidList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);
//
//    @RequestMapping(value="/query/stock/cache/sku/uuids",produces = "application/json;charset=UTF-8")
//    ResultObjectVO queryStockCacheBySkuUuidList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);
//
//    @RequestMapping(method= RequestMethod.POST,value="/deduct/cache/stock",produces = "application/json;charset=UTF-8")
//    ResultObjectVO deductCacheStock(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

}
