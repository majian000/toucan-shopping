package com.toucan.shopping.cloud.order.api.cloud.feign.service;

import com.toucan.shopping.cloud.order.api.cloud.feign.fallback.FeignOrderItemServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-order-proxy/order/orderItem",fallbackFactory = FeignOrderItemServiceFallbackFactory.class)
public interface FeignOrderItemService {

    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/queryAllListByOrderId",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryAllListByOrderId(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/updatesFromOrderList",produces = "application/json;charset=UTF-8")
    ResultObjectVO updatesFromOrderList(@RequestBody RequestJsonVO requestJsonVO);
}
