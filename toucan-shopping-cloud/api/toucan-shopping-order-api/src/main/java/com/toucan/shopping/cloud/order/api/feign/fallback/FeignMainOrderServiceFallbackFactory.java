package com.toucan.shopping.cloud.order.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.order.api.feign.service.FeignMainOrderService;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 订单服务
 */
@Component
public class FeignMainOrderServiceFallbackFactory implements FallbackFactory<FeignMainOrderService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignMainOrderService create(Throwable throwable) {

        logger.warn(throwable.getMessage(),throwable);
        return new FeignMainOrderService(){

            @Override
            public ResultObjectVO create( String signHeader,RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignMainOrderServiceFallbackFactory create header {}  params{}:",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("订单创建失败");
                return resultObjectVO;
            }


            @Override
            public ResultObjectVO cancel(String signHeader,RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignMainOrderServiceFallbackFactory finish  header {}  params{}:",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("取消订单失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryOrderByPayTimeOut(String signHeader,RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignMainOrderServiceFallbackFactory queryOrderByPayTimeOut header {}  params{}:",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询支付超时订单失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryMainOrderByOrderNoAndUserId(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignMainOrderServiceFallbackFactory queryMainOrderByOrderNoAndUserId  params{}:",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询支付超时订单失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryOrderByPayTimeOutPage(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignMainOrderServiceFallbackFactory queryOrderByPayTimeOutPage  params{}:",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询支付超时订单失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO batchCancelPayTimeout(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignMainOrderServiceFallbackFactory batchCancelPayTimeout  params{}:",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询支付超时订单失败");
                return resultObjectVO;
            }
        };
    }
}
