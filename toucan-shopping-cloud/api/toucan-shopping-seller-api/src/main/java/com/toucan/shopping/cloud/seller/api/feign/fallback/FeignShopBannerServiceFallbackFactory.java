package com.toucan.shopping.cloud.seller.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopBannerService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 店铺轮播图
 */
@Component
public class FeignShopBannerServiceFallbackFactory implements FallbackFactory<FeignShopBannerService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignShopBannerService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignShopBannerService(){

        };
    }
}
