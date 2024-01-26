package com.toucan.shopping.modules.user.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.User;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.vo.UserStatisticVO;
import com.toucan.shopping.modules.user.vo.UserVO;

import java.util.List;

/**
 * 用户统计
 * @author majian
 */
public interface UserStatisticService {

    /**
     * 查询 总数/今日新增/本月新增/本年新增
     * @return
     */
    UserStatisticVO queryTotalAndTodayAndCurrentMonthAndCurrentYear();

    /**
     * 查询总数
     * @return
     */
    Long queryTotal();

    /**
     * 查询今日数量
     * @return
     */
    Long queryTodayTotal();

    /**
     * 查询本月数量
     * @return
     */
    Long queryCurMonthTotal();

    /**
     * 查询本年数量
     * @return
     */
    Long queryCurYearTotal();
}
