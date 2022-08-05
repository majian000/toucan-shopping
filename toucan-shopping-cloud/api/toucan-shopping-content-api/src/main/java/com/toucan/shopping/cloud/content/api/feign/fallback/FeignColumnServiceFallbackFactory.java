package com.toucan.shopping.cloud.content.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.content.api.feign.service.FeignColumnService;
import com.toucan.shopping.cloud.content.api.feign.service.FeignColumnTypeService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 栏目类型服务
 */
@Component
public class FeignColumnServiceFallbackFactory implements FallbackFactory<FeignColumnService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignColumnService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignColumnService(){

            @Override
            public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignColumnService.queryListPage失败  params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO save(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignColumnService.save失败  params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

        };
    }
}
