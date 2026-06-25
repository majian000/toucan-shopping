package com.toucan.shopping.cloud.product.api.cloud.feign.service;

import com.toucan.shopping.cloud.product.api.AdminProductSkuServiceAPI;
import com.toucan.shopping.cloud.product.api.cloud.feign.fallback.FeignAdminProductSkuServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-product-proxy/productSku/admin",fallbackFactory = FeignAdminProductSkuServiceFallbackFactory.class)
public interface FeignAdminProductSkuService extends AdminProductSkuServiceAPI {


    @Override
    @RequestMapping(value = "/save",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO saveSku(@RequestBody RequestJsonVO requestJsonVO);


}
