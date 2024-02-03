package com.toucan.shopping.modules.order.mapper;

import com.toucan.shopping.modules.order.page.OrderHotSellPageInfo;
import com.toucan.shopping.modules.order.vo.OrderHotSellStatisticVO;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;


/**
 * 订单统计
 * @author majian
 */
@Mapper
public interface OrderStatisticMapper {

    /**
     * 查询总金额
     * @return
     */
    BigDecimal queryTotalMoney();


    /**
     * 查询今日金额
     * @return
     */
    BigDecimal queryTodayMoney();


    /**
     * 查询当月金额
     * @return
     */
    BigDecimal queryCurMonthMoney();


    /**
     * 查询本年金额
     * @return
     */
    BigDecimal queryCurYearMoney();


    /**
     * 查询热销列表页
     * @param pageInfo
     * @return
     */
    List<OrderHotSellStatisticVO> queryHotSellListPage(OrderHotSellPageInfo pageInfo);

    /**
     * 返回热销列表页数量
     * @param pageInfo
     * @return
     */
    Long queryHotSellListPageCount(OrderHotSellPageInfo pageInfo);

}
