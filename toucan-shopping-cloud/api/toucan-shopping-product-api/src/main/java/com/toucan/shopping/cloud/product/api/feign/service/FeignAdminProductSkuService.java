package com.toucan.shopping.cloud.product.api.feign.service;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.cloud.product.api.feign.fallback.FeignProductSkuServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-product-proxy/productSku/admin",fallbackFactory = FeignProductSkuServiceFallbackFactory.class)
public interface FeignAdminProductSkuService {


    @RequestMapping(value = "/save",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO saveSku(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


}
