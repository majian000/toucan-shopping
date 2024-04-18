package com.toucan.shopping.cloud.order.api.feign.service;

import com.toucan.shopping.cloud.order.api.feign.fallback.FeignOrderExpressDeliveryServiceFallbackFactory;
import com.toucan.shopping.cloud.order.api.feign.fallback.FeignOrderServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-order-proxy/orderExpressDelivery",fallbackFactory = FeignOrderExpressDeliveryServiceFallbackFactory.class)
public interface FeignOrderExpressDeliveryService {


    /**
     * 保存或修改订单快递信息
     */
    @RequestMapping(value="/saveOrUpdate",produces = "application/json;charset=UTF-8")
    ResultObjectVO saveOrUpdate(@RequestBody RequestJsonVO requestJsonVO);



}
