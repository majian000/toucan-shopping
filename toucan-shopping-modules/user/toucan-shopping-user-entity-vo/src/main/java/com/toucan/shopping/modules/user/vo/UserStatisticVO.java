package com.toucan.shopping.modules.user.vo;


import lombok.Data;

/**
 * 用户统计
 */
@Data
public class UserStatisticVO {

    private Long total; //总数

    private Long todayCount; //今日新增

    private Long curMonthCount; //本月新增

    private Long curYearCount; //本年新增

}
