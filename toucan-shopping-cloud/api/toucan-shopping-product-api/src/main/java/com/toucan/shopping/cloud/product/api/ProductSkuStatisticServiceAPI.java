package com.toucan.shopping.cloud.product.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface ProductSkuStatisticServiceAPI {

    /**
     * 查询统计数据
     * 总数 今日新增 本月新增 本年新增
     * @param requestVo
     * @return
     */
    ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear(RequestJsonVO requestVo);

    /**
     * 分类商品统计
     * @param requestVo
     * @return
     */
    ResultObjectVO queryCategoryProductStatistic(RequestJsonVO requestVo);

}
