package com.toucan.shopping.order.api.feign.service;

import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.order.api.feign.fallback.FeignOrderServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-order-proxy/order",fallbackFactory = FeignOrderServiceFallbackFactory.class)
public interface FeignOrderService {

    /**
     * 创建订单
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/create",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO create(@RequestHeader("bbs-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    /**
     * 查询订单下所有skuuuid
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/querySkuUuids/orderNo",produces = "application/json;charset=UTF-8")
    ResultObjectVO querySkuUuidsByOrderNo(@RequestHeader("bbs-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    /**
     * 完成订单
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/finish",produces = "application/json;charset=UTF-8")
    ResultObjectVO finish(@RequestHeader("bbs-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    /**
     * 取消订单
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/cancel",produces = "application/json;charset=UTF-8")
    ResultObjectVO cancel(@RequestHeader("bbs-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 查询支付超时的订单
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/pay/timeout",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryOrderByPayTimeOut(@RequestHeader("bbs-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);
}
