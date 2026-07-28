package com.toucan.shopping.cloud.order.api.cloud.feign.fallback;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.order.api.cloud.feign.service.FeignOrderItemService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 订单项服务
 */
@Component
public class FeignOrderItemServiceFallbackFactory implements FallbackFactory<FeignOrderItemService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignOrderItemService create(Throwable throwable) {

        logger.warn(throwable.getMessage(),throwable);
        return new FeignOrderItemService(){
            @Override
            public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrderItemService queryListPage  params{}:",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询订单项失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryAllListByOrderId(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrderItemService queryAllListByOrderId  params{}:",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询订单项失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO updatesFromOrderList(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrderItemService updatesFromOrderList  params{}:",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询订单项失败");
                return resultObjectVO;
            }
        };
    }
}
