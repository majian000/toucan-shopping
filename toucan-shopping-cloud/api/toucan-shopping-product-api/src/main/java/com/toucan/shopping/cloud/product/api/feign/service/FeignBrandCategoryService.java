package com.toucan.shopping.cloud.product.api.feign.service;

import com.toucan.shopping.cloud.product.api.feign.fallback.FeignBrandCategoryServiceFallbackFactory;
import com.toucan.shopping.cloud.product.api.feign.fallback.FeignBrandServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 品牌分类服务
 * @author majian
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-product-proxy/brand/category",fallbackFactory = FeignBrandCategoryServiceFallbackFactory.class)
public interface FeignBrandCategoryService {



    /**
     * 根据品牌ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/brand/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO findByBrandId(@RequestBody RequestJsonVO requestVo);


}
