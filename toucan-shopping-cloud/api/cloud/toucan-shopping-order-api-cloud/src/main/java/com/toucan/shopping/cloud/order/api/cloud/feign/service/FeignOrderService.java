package com.toucan.shopping.cloud.order.api.cloud.feign.service;

import com.toucan.shopping.cloud.order.api.OrderServiceAPI;
import com.toucan.shopping.cloud.order.api.cloud.feign.fallback.FeignOrderServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-order-proxy/order",fallbackFactory = FeignOrderServiceFallbackFactory.class)
public interface FeignOrderService extends OrderServiceAPI {

    @RequestMapping(value="/querySkuUuids/orderNo",produces = "application/json;charset=UTF-8")
    ResultObjectVO querySkuUuidsByOrderNo(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/finish",produces = "application/json;charset=UTF-8")
    ResultObjectVO finish(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/query/pay/timeout",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryOrderByPayTimeOut(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/queryByOrderNoAndUserId",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByOrderNoAndUserId(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/findById",produces = "application/json;charset=UTF-8")
    ResultObjectVO findById(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/cancel",produces = "application/json;charset=UTF-8")
    ResultObjectVO cancel(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/queryFinishCountByShopId",produces = "application/json;charset=UTF-8")
    ResultTypeObjectVO<Long> queryFinishCountByShopId(@RequestBody RequestJsonVO requestJsonVO);
}
