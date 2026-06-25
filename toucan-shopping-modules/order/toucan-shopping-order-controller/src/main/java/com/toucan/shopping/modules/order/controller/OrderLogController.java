package com.toucan.shopping.modules.order.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
import com.toucan.shopping.modules.order.business.service.OrderLogBusinessService;
import com.toucan.shopping.modules.order.vo.OrderLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order/orderLog")
public class OrderLogController {

    @Autowired
    private OrderLogBusinessService orderLogBusinessService;

    /**
     * 查询列表页
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultPageInfoVO<OrderLogVO> queryListPage(@RequestBody RequestJsonVO requestJsonVO) {
        return orderLogBusinessService.queryListPage(requestJsonVO);
    }
}
