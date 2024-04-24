package com.toucan.shopping.cloud.order.api.feign.service;

import com.toucan.shopping.cloud.order.api.feign.fallback.FeignOrderItemServiceFallbackFactory;
import com.toucan.shopping.cloud.order.api.feign.fallback.FeignOrderLogServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
import com.toucan.shopping.modules.order.vo.OrderLogVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-order-proxy/order/orderLog",fallbackFactory = FeignOrderLogServiceFallbackFactory.class)
public interface FeignOrderLogService {

    /**
     * 查询列表页
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    ResultPageInfoVO<OrderLogVO> queryListPage(@RequestBody RequestJsonVO requestJsonVO);

}
