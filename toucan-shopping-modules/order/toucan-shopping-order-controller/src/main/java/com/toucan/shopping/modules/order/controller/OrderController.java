package com.toucan.shopping.modules.order.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.order.business.service.OrderBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderBusinessService orderBusinessService;

    /**
     * 测试分片
     */
    @RequestMapping(value="/testSharding", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO testSharding(@RequestBody RequestJsonVO requestJsonVO) throws Exception {
        return orderBusinessService.testSharding(requestJsonVO);
    }

    /**
     * 取消订单
     */
    @RequestMapping(value="/cancel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO cancel(@RequestBody RequestJsonVO requestJsonVO) {
        return orderBusinessService.cancel(requestJsonVO);
    }

    /**
     * 根据订单编号查询所有skuid
     */
    @RequestMapping(value="/querySkuUuids/orderNo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO querySkuUuidsByOrderNo(@RequestBody RequestJsonVO requestJsonVO) {
        return orderBusinessService.querySkuUuidsByOrderNo(requestJsonVO);
    }

    /**
     * 查询支付超时订单
     */
    @RequestMapping(value="/query/pay/timeout", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryOrderByPayTimeOut(@RequestBody RequestJsonVO requestJsonVO) {
        return orderBusinessService.queryOrderByPayTimeOut(requestJsonVO);
    }

    /**
     * 更新订单
     */
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO) {
        return orderBusinessService.update(requestJsonVO);
    }

    /**
     * 查询支付超时订单页
     */
    @RequestMapping(value="/query/pay/timeout/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryOrderByPayTimeOutPage(@RequestBody RequestJsonVO requestJsonVO) {
        return orderBusinessService.queryOrderByPayTimeOutPage(requestJsonVO);
    }

    /**
     * 完成订单
     */
    @RequestMapping(value="/finish", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO finish(@RequestBody RequestJsonVO requestJsonVO) {
        return orderBusinessService.finish(requestJsonVO);
    }

    /**
     * 查询列表页
     */
    @RequestMapping(value="/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO) {
        return orderBusinessService.queryListPage(requestJsonVO);
    }

    /**
     * 查询子订单
     */
    @RequestMapping(value="/queryByOrderNoAndUserId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryByOrderNoAndUserId(@RequestBody RequestJsonVO requestJsonVO) {
        return orderBusinessService.queryByOrderNoAndUserId(requestJsonVO);
    }

    /**
     * 根据ID查询
     */
    @RequestMapping(value="/findById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestJsonVO) {
        return orderBusinessService.findById(requestJsonVO);
    }

    /**
     * 查询已完成订单数量
     */
    @RequestMapping(value="/queryFinishCountByShopId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultTypeObjectVO<Long> queryFinishCountByShopId(@RequestBody RequestJsonVO requestJsonVO) {
        return orderBusinessService.queryFinishCountByShopId(requestJsonVO);
    }
}
