package com.toucan.shopping.cloud.user.api.cloud.feign.service;

import com.toucan.shopping.cloud.user.api.UserStatisticServiceAPI;
import com.toucan.shopping.cloud.user.api.cloud.feign.fallback.FeignUserStatisticServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户统计
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-user-proxy/userStatistic",fallbackFactory = FeignUserStatisticServiceFallbackFactory.class)
public interface FeignUserStatisticService extends UserStatisticServiceAPI {



    /**
     * 查询统计数据
     * 总数 今日新增 本月新增 本年新增
     * @return
     */
    @Override
    @RequestMapping(value = "/queryTotalAndTodayAndCurrentMonthAndCurrentYear",method = RequestMethod.POST)
    ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear(RequestJsonVO requestVo);




    /**
     * 刷新用户总数
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value = "/refershTotal",method = RequestMethod.POST)
    ResultObjectVO refershTotal(RequestJsonVO requestVo);

}
