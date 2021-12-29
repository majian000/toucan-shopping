package com.toucan.shopping.cloud.message.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.message.api.feign.service.FeignMessageService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 消息服务
 */
@Component
public class FeignMessageServiceFallbackFactory implements FallbackFactory<FeignMessageService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignMessageService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignMessageService(){

            @Override
            public ResultObjectVO send(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignMessageService.save失败  params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }
        };
    }
}
