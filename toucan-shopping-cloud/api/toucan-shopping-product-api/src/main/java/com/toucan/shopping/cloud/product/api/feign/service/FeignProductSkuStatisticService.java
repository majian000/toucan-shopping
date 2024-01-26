package com.toucan.shopping.cloud.product.api.feign.service;

import com.toucan.shopping.cloud.product.api.feign.fallback.FeignProductSkuServiceFallbackFactory;
import com.toucan.shopping.cloud.product.api.feign.fallback.FeignProductSkuStatisticServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultListVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-product-proxy/productSkuStatistic",fallbackFactory = FeignProductSkuStatisticServiceFallbackFactory.class)
public interface FeignProductSkuStatisticService {


    /**
     * 查询统计数据
     * 总数 今日新增 本月新增 本年新增
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/queryTotalAndTodayAndCurrentMonthAndCurrentYear",method = RequestMethod.POST)
    ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear(RequestJsonVO requestVo);



    /**
     * 分类商品统计
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/queryCategoryProductStatistic",method = RequestMethod.POST)
    ResultObjectVO queryCategoryProductStatistic(@RequestBody RequestJsonVO requestVo);

}
