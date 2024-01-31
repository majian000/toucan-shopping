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
     * 总金额
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/queryTotalAndTodayAndCurrentMonthAndCurrentYear",method = RequestMethod.POST)
    ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear(RequestJsonVO requestVo);



}
