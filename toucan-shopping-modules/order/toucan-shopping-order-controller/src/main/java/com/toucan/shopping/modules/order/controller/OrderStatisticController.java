package com.toucan.shopping.modules.order.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.order.business.service.OrderStatisticBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping("/orderStatistic")
public class OrderStatisticController {

    @Autowired
    private OrderStatisticBusinessService orderStatisticBusinessService;

    /**
     * 总金额
     */
    @RequestMapping(value = "/queryTotalAndTodayAndCurrentMonthAndCurrentYear",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear(@RequestBody RequestJsonVO requestVo) {
        return orderStatisticBusinessService.queryTotalAndTodayAndCurrentMonthAndCurrentYear(requestVo);
    }

    /**
     * 查询热销列表
     */
    @RequestMapping(value="/query/hot/sell/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryHotSellListPage(@RequestBody RequestJsonVO requestJsonVO) {
        return orderStatisticBusinessService.queryHotSellListPage(requestJsonVO);
    }
}
