package com.toucan.shopping.area.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.area.api.feign.service.FeignAdminAreaService;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 管理员地区服务
 */
@Component
public class FeignAdminAreaServiceFallbackFactory implements FallbackFactory<FeignAdminAreaService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignAdminAreaService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignAdminAreaService(){


            @Override
            public ResultObjectVO save(String signHeader, RequestJsonVO requestJsonVO) {

                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignAdminAreaService save faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败");
                return resultObjectVO;
            }
        };
    }
}
