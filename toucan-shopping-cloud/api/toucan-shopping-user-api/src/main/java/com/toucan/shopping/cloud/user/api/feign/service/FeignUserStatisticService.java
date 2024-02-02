package com.toucan.shopping.cloud.user.api.feign.service;

import com.toucan.shopping.cloud.user.api.feign.fallback.FeignUserServiceFallbackFactory;
import com.toucan.shopping.cloud.user.api.feign.fallback.FeignUserStatisticServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户统计
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-user-proxy/userStatistic",fallbackFactory = FeignUserStatisticServiceFallbackFactory.class)
public interface FeignUserStatisticService {



    /**
     * 查询统计数据
     * 总数 今日新增 本月新增 本年新增
     * @return
     */
    @RequestMapping(value = "/queryTotalAndTodayAndCurrentMonthAndCurrentYear",method = RequestMethod.POST)
    ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear(RequestJsonVO requestVo);




    /**
     * 刷新用户总数
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/refershTotal",method = RequestMethod.POST)
    ResultObjectVO refershTotal(RequestJsonVO requestVo);

}
