package com.toucan.shopping.cloud.order.api.cloud.feign.service;

import com.toucan.shopping.cloud.order.api.cloud.feign.fallback.FeignOrderExpressDeliveryServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.order.vo.OrderExpressDeliveryVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-order-proxy/orderExpressDelivery",fallbackFactory = FeignOrderExpressDeliveryServiceFallbackFactory.class)
public interface FeignOrderExpressDeliveryService {

    @RequestMapping(value="/saveOrUpdate",produces = "application/json;charset=UTF-8")
    ResultObjectVO saveOrUpdate(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/removeByOrderId",produces = "application/json;charset=UTF-8")
    ResultObjectVO removeByOrderId(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/findOneByOrderIdAndShopId",produces = "application/json;charset=UTF-8")
    ResultTypeObjectVO<OrderExpressDeliveryVO> findOneByOrderIdAndShopId(@RequestBody RequestJsonVO requestJsonVO);
}
