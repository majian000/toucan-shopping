package com.toucan.shopping.modules.product.service;

import com.toucan.shopping.modules.product.vo.ProductSkuStatisticVO;

/**
 * 商品统计
 * @author majian
 */
public interface ProductSkuStatisticService {



    /**
     * 查询 总数/今日新增/本月新增/本年新增
     * @return
     */
    ProductSkuStatisticVO queryTotalAndTodayAndCurrentMonthAndCurrentYear();

}
