package com.toucan.shopping.cloud.order.api.cloud.feign.service;

import com.toucan.shopping.cloud.order.api.cloud.feign.fallback.FeignMainOrderServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-order-proxy/main/order",fallbackFactory = FeignMainOrderServiceFallbackFactory.class)
public interface FeignMainOrderService {

    @RequestMapping(value = "/create",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO create(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/cancel",produces = "application/json;charset=UTF-8")
    ResultObjectVO cancel(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/query/pay/timeout",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryOrderByPayTimeOut(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/queryMainOrderByOrderNoAndUserId",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryMainOrderByOrderNoAndUserId(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/query/pay/timeout/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryOrderByPayTimeOutPage(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/batch/cancel/pay/timeout",produces = "application/json;charset=UTF-8")
    ResultObjectVO batchCancelPayTimeout(@RequestBody RequestJsonVO requestJsonVO);
}
