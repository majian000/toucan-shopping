package com.toucan.shopping.cloud.order.api.cloud.feign.service;

import com.toucan.shopping.cloud.order.api.cloud.feign.fallback.FeignOrderStatisticServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-order-proxy/orderStatistic",fallbackFactory = FeignOrderStatisticServiceFallbackFactory.class)
public interface FeignOrderStatisticService {

    @RequestMapping(value = "/queryTotalAndTodayAndCurrentMonthAndCurrentYear",method = RequestMethod.POST)
    ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear(RequestJsonVO requestVo);

    @RequestMapping(value="/query/hot/sell/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryHotSellListPage(@RequestBody RequestJsonVO requestJsonVO);
}
