package com.toucan.shopping.cloud.order.api.single;

import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderStatisticService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.order.business.service.OrderStatisticBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderStatisticServiceSingleImpl implements FeignOrderStatisticService {

    @Autowired
    private OrderStatisticBusinessService orderStatisticBusinessService;

    @Override
    public ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear(RequestJsonVO requestVo) {
        return orderStatisticBusinessService.queryTotalAndTodayAndCurrentMonthAndCurrentYear(requestVo);
    }

    @Override
    public ResultObjectVO queryHotSellListPage(RequestJsonVO requestJsonVO) {
        return orderStatisticBusinessService.queryHotSellListPage(requestJsonVO);
    }
}
