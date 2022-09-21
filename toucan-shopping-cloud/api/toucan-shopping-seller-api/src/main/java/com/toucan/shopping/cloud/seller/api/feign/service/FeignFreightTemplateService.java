package com.toucan.shopping.cloud.seller.api.feign.service;

import com.toucan.shopping.cloud.seller.api.feign.fallback.FeignFreightTemplateServiceFallbackFactory;
import com.toucan.shopping.cloud.seller.api.feign.fallback.FeignSellerShopServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 运费模板
 * @author majian
 * @date 2022-9-21 14:14:06
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-seller-proxy/freightTemplate",fallbackFactory = FeignFreightTemplateServiceFallbackFactory.class)
public interface FeignFreightTemplateService {



    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo);



}
