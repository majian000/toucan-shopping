package com.toucan.shopping.cloud.product.api.feign.service;

import com.toucan.shopping.cloud.product.api.feign.fallback.FeignAttributeKeyServiceFallbackFactory;
import com.toucan.shopping.cloud.product.api.feign.fallback.FeignAttributeKeyValueServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-product-proxy/attributeKeyValue",fallbackFactory = FeignAttributeKeyValueServiceFallbackFactory.class)
public interface FeignAttributeKeyValueService {


    /**
     * 根据分类ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/category/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    public ResultObjectVO findByCategoryId(@RequestBody RequestJsonVO requestVo);

}
