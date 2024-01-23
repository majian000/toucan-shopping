package com.toucan.shopping.cloud.user.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserStatisticService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.entity.User;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 用户统计
 */
@Component
public class FeignUserStatisticServiceFallbackFactory implements FallbackFactory<FeignUserStatisticService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignUserStatisticService create(Throwable throwable) {
        logger.error(throwable.getMessage(),throwable);
        return new FeignUserStatisticService(){
            @Override
            public ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                logger.warn("调用FeignUserStatisticService.queryTotalAndTodayAndCurrentMonthAndCurrentYear失败");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }
        };
    }
}
