package com.toucan.shopping.cloud.order.api.feign.service;

import com.toucan.shopping.cloud.order.api.feign.fallback.FeignOrderServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-order-proxy/order",fallbackFactory = FeignOrderServiceFallbackFactory.class)
public interface FeignOrderService {


    /**
     * 查询订单下所有skuuuid
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/querySkuUuids/orderNo",produces = "application/json;charset=UTF-8")
    ResultObjectVO querySkuUuidsByOrderNo(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    /**
     * 完成订单
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/finish",produces = "application/json;charset=UTF-8")
    ResultObjectVO finish(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);



    /**
     * 查询支付超时的订单
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/pay/timeout",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryOrderByPayTimeOut(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);




    /**
     * 查询列表页
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 查询子订单
     */
    @RequestMapping(value="/queryByOrderNoAndUserId",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByOrderNoAndUserId(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据ID查询
     */
    @RequestMapping(value="/findById",produces = "application/json;charset=UTF-8")
    ResultObjectVO findById(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 取消订单
     */
    @RequestMapping(value="/cancel",produces = "application/json;charset=UTF-8")
    ResultObjectVO cancel(@RequestBody RequestJsonVO requestJsonVO);

}
