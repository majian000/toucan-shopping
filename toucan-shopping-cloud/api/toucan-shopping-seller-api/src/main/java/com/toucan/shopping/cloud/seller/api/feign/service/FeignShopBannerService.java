package com.toucan.shopping.cloud.seller.api.feign.service;

import com.toucan.shopping.cloud.seller.api.feign.fallback.FeignShopBannerServiceFallbackFactory;
import com.toucan.shopping.cloud.seller.api.feign.fallback.FeignShopCategoryServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-seller-proxy/shop/banner",fallbackFactory = FeignShopBannerServiceFallbackFactory.class)
public interface FeignShopBannerService {



}
