package com.toucan.shopping.cloud.order.api.cloud.feign.service;

import com.toucan.shopping.cloud.order.api.OrderRefundServiceAPI;
import com.toucan.shopping.cloud.order.api.cloud.feign.fallback.FeignOrderRefundServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
import com.toucan.shopping.modules.order.vo.OrderRefundVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-order-proxy/order/orderRefund",fallbackFactory = FeignOrderRefundServiceFallbackFactory.class)
public interface FeignOrderRefundService extends OrderRefundServiceAPI {

    @RequestMapping(value="/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultPageInfoVO<OrderRefundVO> queryListPage(@RequestBody RequestJsonVO requestJsonVO);
}
