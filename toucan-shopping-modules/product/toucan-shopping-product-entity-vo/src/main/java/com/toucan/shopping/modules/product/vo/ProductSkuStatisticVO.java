package com.toucan.shopping.modules.product.vo;


import lombok.Data;

/**
 * 商品SKU统计
 * @author majian
 */
@Data
public class ProductSkuStatisticVO {

    private Long total; //总数

    private Long todayCount; //今日新增

    private Long curMonthCount; //本月新增

    private Long curYearCount; //本年新增

}
