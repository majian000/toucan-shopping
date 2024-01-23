package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.User;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserStatisticMapper {

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
