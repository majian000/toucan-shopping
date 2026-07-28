package com.toucan.shopping.cloud.content.api.cloud.feign.fallback;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.content.api.cloud.feign.service.FeignColumnAreaService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FeignColumnAreaServiceFallbackFactory implements FallbackFactory<FeignColumnAreaService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignColumnAreaService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignColumnAreaService(){

            @Override
            public ResultObjectVO queryColumnAreaList(RequestJsonVO requestJsonVO) {

                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignColumnAreaService.queryColumnAreaList failed params:{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败");
                return resultObjectVO;
            }
        };
    }
}
