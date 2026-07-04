package com.toucan.shopping.cloud.order.api.cloud.feign.service;

import com.toucan.shopping.cloud.order.api.MainOrderServiceAPI;
import com.toucan.shopping.cloud.order.api.cloud.feign.fallback.FeignMainOrderServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-order-proxy/main/order",fallbackFactory = FeignMainOrderServiceFallbackFactory.class)
public interface FeignMainOrderService extends MainOrderServiceAPI {

    @RequestMapping(value = "/create",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO create(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/cancel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO cancel(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/query/pay/timeout", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryOrderByPayTimeOut(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/queryMainOrderByOrderNoAndUserId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryMainOrderByOrderNoAndUserId(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/query/pay/timeout/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryOrderByPayTimeOutPage(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/batch/cancel/pay/timeout", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO batchCancelPayTimeout(@RequestBody RequestJsonVO requestJsonVO);
}
