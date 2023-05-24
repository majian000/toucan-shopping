package com.toucan.shopping.cloud.order.api.feign.service;

import com.toucan.shopping.cloud.order.api.feign.fallback.FeignMainOrderServiceFallbackFactory;
import com.toucan.shopping.cloud.order.api.feign.fallback.FeignOrderServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-order-proxy/main/order",fallbackFactory = FeignMainOrderServiceFallbackFactory.class)
public interface FeignMainOrderService {

    /**
     * 创建订单
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/create",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO create(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);


    /**
     * 取消订单
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/cancel",produces = "application/json;charset=UTF-8")
    ResultObjectVO cancel(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 查询支付超时的订单
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/pay/timeout",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryOrderByPayTimeOut(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 查询主订单
     */
    @RequestMapping(value="/queryMainOrderByOrderNoAndUserId",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryMainOrderByOrderNoAndUserId(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 查询支付超时订单页
     */
    @RequestMapping(value="/query/pay/timeout/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryOrderByPayTimeOutPage(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 批量取消支付超时订单
     */
    @RequestMapping(value="/batch/cancel/pay/timeout",produces = "application/json;charset=UTF-8")
    ResultObjectVO batchCancelPayTimeout(@RequestBody RequestJsonVO requestJsonVO);

}
