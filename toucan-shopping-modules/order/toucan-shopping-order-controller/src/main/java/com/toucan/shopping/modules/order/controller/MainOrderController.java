package com.toucan.shopping.modules.order.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.order.business.service.MainOrderBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/main/order")
public class MainOrderController {

    @Autowired
    private MainOrderBusinessService mainOrderBusinessService;

    /**
     * 测试分片
     */
    @RequestMapping(value="/testSharding",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO testSharding(@RequestBody RequestJsonVO requestJsonVO) throws Exception {
        return mainOrderBusinessService.testSharding(requestJsonVO);
    }

    /**
     * 创建订单
     */
    @RequestMapping(value="/create",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO create( @RequestBody RequestJsonVO requestJsonVO) {
        return mainOrderBusinessService.create( requestJsonVO);
    }

    /**
     * 取消订单
     */
    @RequestMapping(value="/cancel",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO cancel(@RequestBody RequestJsonVO requestJsonVO) {
        return mainOrderBusinessService.cancel(requestJsonVO);
    }

    /**
     * 查询主订单
     */
    @RequestMapping(value="/queryMainOrderByOrderNoAndUserId",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryMainOrderByOrderNoAndUserId(@RequestBody RequestJsonVO requestJsonVO) {
        return mainOrderBusinessService.queryMainOrderByOrderNoAndUserId(requestJsonVO);
    }

    /**
     * 查询支付超时订单页
     */
    @RequestMapping(value="/query/pay/timeout/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryOrderByPayTimeOutPage(@RequestBody RequestJsonVO requestJsonVO) {
        return mainOrderBusinessService.queryOrderByPayTimeOutPage(requestJsonVO);
    }

    /**
     * 批量取消支付超时订单
     */
    @RequestMapping(value="/batch/cancel/pay/timeout",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO batchCancelPayTimeout(@RequestBody RequestJsonVO requestJsonVO) {
        return mainOrderBusinessService.batchCancelPayTimeout(requestJsonVO);
    }
}
