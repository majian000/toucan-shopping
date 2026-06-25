package com.toucan.shopping.cloud.user.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

/**
 * 用户统计
 */
public interface UserStatisticServiceAPI {



    /**
     * 查询统计数据
     * 总数 今日新增 本月新增 本年新增
     * @return
     */
    ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear(RequestJsonVO requestVo);




    /**
     * 刷新用户总数
     * @param requestVo
     * @return
     */
    ResultObjectVO refershTotal(RequestJsonVO requestVo);

}
