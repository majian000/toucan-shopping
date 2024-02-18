package com.toucan.shopping.cloud.order.api.feign.service;

import com.toucan.shopping.cloud.order.api.feign.fallback.FeignOrderItemServiceFallbackFactory;
import com.toucan.shopping.cloud.order.api.feign.fallback.FeignOrderServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-order-proxy/order/orderItem",fallbackFactory = FeignOrderItemServiceFallbackFactory.class)
public interface FeignOrderItemService {


    /**
     * 查询列表页
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);



    @RequestMapping(value="/queryAllListByOrderId",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryAllListByOrderId(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 修改订单项(从订单列表)
     */
    @RequestMapping(value="/updatesFromOrderList",produces = "application/json;charset=UTF-8")
    ResultObjectVO updatesFromOrderList(@RequestBody RequestJsonVO requestJsonVO);

}
