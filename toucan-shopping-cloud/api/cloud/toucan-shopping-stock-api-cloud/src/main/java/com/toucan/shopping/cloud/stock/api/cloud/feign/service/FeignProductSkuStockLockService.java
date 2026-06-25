package com.toucan.shopping.cloud.stock.api.cloud.feign.service;

import com.toucan.shopping.cloud.stock.api.cloud.feign.fallback.FeignProductSkuStockLockServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-stock-proxy/productSkuStockLock",fallbackFactory = FeignProductSkuStockLockServiceFallbackFactory.class)
public interface FeignProductSkuStockLockService {

    @RequestMapping(value="/lock/stock",produces = "application/json;charset=UTF-8")
    ResultObjectVO lockStock(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/find/lock/stock/num",produces = "application/json;charset=UTF-8")
    ResultObjectVO findLockStockNumByProductSkuIds(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/delete/lock/stock",produces = "application/json;charset=UTF-8")
    ResultObjectVO deleteLockStock(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/find/lock/stock/num/by/mainOrderNos",produces = "application/json;charset=UTF-8")
    ResultObjectVO findLockStockNumByMainOrderNos(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/delete/lock/stock/by/mainOrderNos",produces = "application/json;charset=UTF-8")
    ResultObjectVO deleteLockStockByMainOrderNos(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/find/lock/stock/num/by/orderNo",produces = "application/json;charset=UTF-8")
    ResultObjectVO findLockStockNumByOrderNo(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/delete/lock/stock/by/orderNo",produces = "application/json;charset=UTF-8")
    ResultObjectVO deleteLockStockByOrderNo(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/find/lock/stock/list/by/mainOrderNos",produces = "application/json;charset=UTF-8")
    ResultObjectVO findLockStockListByMainOrderNos(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/find/by/id",produces = "application/json;charset=UTF-8")
    ResultObjectVO findById(@RequestBody RequestJsonVO requestJsonVO);
}
