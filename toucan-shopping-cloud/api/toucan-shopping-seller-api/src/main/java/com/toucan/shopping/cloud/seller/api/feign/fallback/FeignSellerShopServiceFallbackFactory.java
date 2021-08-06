package com.toucan.shopping.cloud.seller.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 商户店铺服务
 */
@Component
public class FeignSellerShopServiceFallbackFactory implements FallbackFactory<FeignSellerShopService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignSellerShopService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignSellerShopService(){


        };
    }
}
