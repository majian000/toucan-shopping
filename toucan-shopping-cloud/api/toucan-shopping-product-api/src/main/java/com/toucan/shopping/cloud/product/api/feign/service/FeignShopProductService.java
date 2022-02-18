package com.toucan.shopping.cloud.product.api.feign.service;

import com.toucan.shopping.cloud.product.api.feign.fallback.FeignProductSkuServiceFallbackFactory;
import com.toucan.shopping.cloud.product.api.feign.fallback.FeignShopProductServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultListVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-product-proxy/shopProduct",fallbackFactory = FeignShopProductServiceFallbackFactory.class)
public interface FeignShopProductService {


    /**
     * 发布商品
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/publish",produces = "application/json;charset=UTF-8")
    ResultObjectVO publish(@RequestBody RequestJsonVO requestJsonVO);




}
