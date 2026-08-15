package com.toucan.shopping.cloud.order.api.cloud.feign.service;

import com.toucan.shopping.cloud.order.api.OrderPayServiceAPI;
import com.toucan.shopping.cloud.order.api.cloud.feign.fallback.FeignOrderPayServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
import com.toucan.shopping.modules.order.vo.OrderPayVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-order-proxy/order/orderPay",fallbackFactory = FeignOrderPayServiceFallbackFactory.class)
public interface FeignOrderPayService extends OrderPayServiceAPI {

    @RequestMapping(value="/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultPageInfoVO<OrderPayVO> queryListPage(@RequestBody RequestJsonVO requestJsonVO);
}
