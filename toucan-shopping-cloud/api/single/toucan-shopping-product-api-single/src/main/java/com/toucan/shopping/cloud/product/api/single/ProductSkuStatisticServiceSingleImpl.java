package com.toucan.shopping.cloud.product.api.single;

import com.toucan.shopping.cloud.product.api.ProductSkuStatisticServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.business.service.ProductSkuStatisticBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductSkuStatisticServiceSingleImpl implements ProductSkuStatisticServiceAPI {

    @Autowired
    private ProductSkuStatisticBusinessService productSkuStatisticBusinessService;

    @Override
    public ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear(RequestJsonVO requestVo) {
        return productSkuStatisticBusinessService.queryTotalAndTodayAndCurrentMonthAndCurrentYear(requestVo);
    }

    @Override
    public ResultObjectVO queryCategoryProductStatistic(RequestJsonVO requestVo) {
        return productSkuStatisticBusinessService.queryCategoryProductStatistic(requestVo);
    }
}
