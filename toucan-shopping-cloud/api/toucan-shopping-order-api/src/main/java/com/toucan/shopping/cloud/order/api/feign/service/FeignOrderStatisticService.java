package com.toucan.shopping.cloud.order.api.feign.service;

import com.toucan.shopping.cloud.order.api.feign.fallback.FeignOrderStatisticServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-order-proxy/orderStatistic",fallbackFactory = FeignOrderStatisticServiceFallbackFactory.class)
public interface FeignOrderStatisticService {


    /**
     * 查询统计数据
     * 总数 今日新增 本月新增 本年新增
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/queryTotalAndTodayAndCurrentMonthAndCurrentYear",method = RequestMethod.POST)
    ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear(RequestJsonVO requestVo);



}
