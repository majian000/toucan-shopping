package com.toucan.shopping.cloud.product.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.product.api.feign.service.FeignAdminProductSkuService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 商品服务
 */
@Component
public class FeignAdminProductSkuServiceFallbackFactory implements FallbackFactory<FeignAdminProductSkuService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignAdminProductSkuService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignAdminProductSkuService(){
            @Override
            public ResultObjectVO saveSku(String signHeader,RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignAdminProductSkuService save faild  params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存SKU失败");
                return resultObjectVO;
            }
        };
    }
}
