package com.toucan.shopping.product.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultListVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.product.api.feign.service.FeignAdminProductSkuService;
import com.toucan.shopping.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.product.export.entity.ProductSku;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

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
