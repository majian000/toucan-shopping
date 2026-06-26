package com.toucan.shopping.cloud.content.api.cloud.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.content.api.cloud.feign.service.FeignBannerAreaService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FeignBannerAreaServiceFallbackFactory implements FallbackFactory<FeignBannerAreaService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignBannerAreaService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignBannerAreaService(){

            @Override
            public ResultObjectVO queryBannerAreaList(RequestJsonVO requestJsonVO) {

                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignBannerAreaService.queryBannerAreaList failed params:{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败");
                return resultObjectVO;
            }
        };
    }
}
