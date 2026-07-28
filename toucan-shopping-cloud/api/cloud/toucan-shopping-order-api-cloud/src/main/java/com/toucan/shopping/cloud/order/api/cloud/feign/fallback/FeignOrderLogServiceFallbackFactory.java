package com.toucan.shopping.cloud.order.api.cloud.feign.fallback;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.order.api.cloud.feign.service.FeignOrderLogService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
import com.toucan.shopping.modules.order.vo.OrderLogVO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 订单日志
 */
@Component
public class FeignOrderLogServiceFallbackFactory implements FallbackFactory<FeignOrderLogService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignOrderLogService create(Throwable throwable) {

        logger.warn(throwable.getMessage(),throwable);
        return new FeignOrderLogService(){

            @Override
            public ResultPageInfoVO<OrderLogVO> queryListPage(RequestJsonVO requestJsonVO) {
                ResultPageInfoVO<OrderLogVO> resultObjectVO = new ResultPageInfoVO<OrderLogVO>();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrderLogService queryListPage  params{}:",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询订单项失败");
                return resultObjectVO;
            }
        };
    }
}
