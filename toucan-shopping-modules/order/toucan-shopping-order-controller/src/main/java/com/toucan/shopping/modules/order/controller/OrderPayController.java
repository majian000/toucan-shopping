package com.toucan.shopping.modules.order.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
import com.toucan.shopping.modules.order.business.service.OrderPayBusinessService;
import com.toucan.shopping.modules.order.vo.OrderPayVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping("/order/orderPay")
public class OrderPayController {

    @Autowired
    private OrderPayBusinessService orderPayBusinessService;

    /**
     * 查询列表页
     */
    @RequestMapping(value="/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultPageInfoVO<OrderPayVO> queryListPage(@RequestBody RequestJsonVO requestJsonVO) {
        return orderPayBusinessService.queryListPage(requestJsonVO);
    }
}
