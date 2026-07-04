package com.toucan.shopping.cloud.order.api.cloud.feign.service;

import com.toucan.shopping.cloud.order.api.OrderItemServiceAPI;
import com.toucan.shopping.cloud.order.api.cloud.feign.fallback.FeignOrderItemServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-order-proxy/order/orderItem",fallbackFactory = FeignOrderItemServiceFallbackFactory.class)
public interface FeignOrderItemService extends OrderItemServiceAPI {

    @RequestMapping(value="/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/queryAllListByOrderId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryAllListByOrderId(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/updatesFromOrderList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO updatesFromOrderList(@RequestBody RequestJsonVO requestJsonVO);
}
