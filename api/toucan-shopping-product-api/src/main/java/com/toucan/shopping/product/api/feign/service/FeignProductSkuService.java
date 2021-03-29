package com.toucan.shopping.product.api.feign.service;

import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultListVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.product.api.feign.fallback.FeignProductSkuServiceFallbackFactory;
import com.toucan.shopping.product.export.entity.ProductSku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-product-proxy/productSku",fallbackFactory = FeignProductSkuServiceFallbackFactory.class)
public interface FeignProductSkuService {

    @RequestMapping(value = "/shelves/list",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultListVO queryShelvesList(@RequestHeader("bbs-sign-header") String signHeader,@RequestBody  RequestJsonVO requestJsonVO);


    @RequestMapping(value = "/query/id",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryById(@RequestBody RequestJsonVO requestJsonVO);


    @RequestMapping(value = "/query/ids",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByIdList(@RequestHeader("bbs-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

}
