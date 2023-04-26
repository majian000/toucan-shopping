package com.toucan.shopping.cloud.search.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.search.api.feign.service.FeignProductSearchService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 商品搜索
 */
@Component
public class FeignProductSearchServiceFallbackFactory implements FallbackFactory<FeignProductSearchService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignProductSearchService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignProductSearchService(){

            @Override
            public ResultObjectVO search(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.error("FeignProductSearchService search faild  params:{}", JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败");
                return resultObjectVO;
            }
        };
    }
}