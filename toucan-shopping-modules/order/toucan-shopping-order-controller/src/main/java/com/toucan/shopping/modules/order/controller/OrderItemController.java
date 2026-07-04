package com.toucan.shopping.modules.order.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.order.business.service.OrderItemBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping("/order/orderItem")
public class OrderItemController {

    @Autowired
    private OrderItemBusinessService orderItemBusinessService;

    /**
     * 查询列表页
     */
    @RequestMapping(value="/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO) {
        return orderItemBusinessService.queryListPage(requestJsonVO);
    }

    /**
     * 查询列表页
     */
    @RequestMapping(value="/queryAllListByOrderId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryAllListByOrderId(@RequestBody RequestJsonVO requestJsonVO) {
        return orderItemBusinessService.queryAllListByOrderId(requestJsonVO);
    }

    /**
     * 修改订单项(从订单列表)
     */
    @RequestMapping(value="/updatesFromOrderList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updatesFromOrderList(@RequestBody RequestJsonVO requestJsonVO) {
        return orderItemBusinessService.updatesFromOrderList(requestJsonVO);
    }
}
