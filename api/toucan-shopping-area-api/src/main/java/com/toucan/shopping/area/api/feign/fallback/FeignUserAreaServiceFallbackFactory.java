package com.toucan.shopping.area.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.area.api.feign.service.FeignAdminAreaService;
import com.toucan.shopping.area.api.feign.service.FeignUserAreaService;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 用户地区服务
 */
@Component
public class FeignUserAreaServiceFallbackFactory implements FallbackFactory<FeignUserAreaService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignUserAreaService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignUserAreaService(){


            @Override
            public ResultObjectVO queryAll(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignUserAreaService.queryAll失败 header:{} params:{}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败");
                return resultObjectVO;
            }




        };
    }
}
