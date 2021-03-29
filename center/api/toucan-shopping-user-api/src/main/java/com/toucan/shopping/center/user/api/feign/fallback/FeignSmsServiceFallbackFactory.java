package com.toucan.shopping.center.user.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.center.user.export.vo.UserSmsVO;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.center.user.api.feign.service.FeignSmsService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 短信服务
 */
@Component
public class FeignSmsServiceFallbackFactory implements FallbackFactory<FeignSmsService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignSmsService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignSmsService(){

            @Override
            public ResultObjectVO send(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignSmsService.send失败 signHeader{} params{}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }
        };
    }
}
