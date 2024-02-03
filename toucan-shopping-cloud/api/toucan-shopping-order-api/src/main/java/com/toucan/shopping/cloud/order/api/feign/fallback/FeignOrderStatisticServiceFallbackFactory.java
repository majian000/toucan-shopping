package com.toucan.shopping.cloud.order.api.feign.fallback;

import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderStatisticService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 订单统计
 * @author majian
 */
@Component
public class FeignOrderStatisticServiceFallbackFactory implements FallbackFactory<FeignOrderStatisticService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignOrderStatisticService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignOrderStatisticService(){

            @Override
            public ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                logger.warn("调用FeignOrderStatisticService.queryTotalAndTodayAndCurrentMonthAndCurrentYear失败");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryHotSellListPage(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                logger.warn("调用FeignOrderStatisticService.queryHotSellListPage失败");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

        };
    }
}
