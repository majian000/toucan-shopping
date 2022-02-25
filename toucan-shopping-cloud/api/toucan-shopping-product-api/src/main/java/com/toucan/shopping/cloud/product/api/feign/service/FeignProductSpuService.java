package com.toucan.shopping.cloud.product.api.feign.service;

import com.toucan.shopping.cloud.product.api.feign.fallback.FeignProductSkuServiceFallbackFactory;
import com.toucan.shopping.cloud.product.api.feign.fallback.FeignProductSpuServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultListVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-product-proxy/productSpu",fallbackFactory = FeignProductSpuServiceFallbackFactory.class)
public interface FeignProductSpuService {



    /**
     * 保存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);
}
