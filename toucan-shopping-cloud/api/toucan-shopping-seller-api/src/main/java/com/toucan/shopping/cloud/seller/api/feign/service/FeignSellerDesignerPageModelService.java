package com.toucan.shopping.cloud.seller.api.feign.service;

import com.toucan.shopping.cloud.seller.api.feign.fallback.FeignSellerDesignerPageModelServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-seller-proxy/seller/designer/page/model",fallbackFactory = FeignSellerDesignerPageModelServiceFallbackFactory.class)
public interface FeignSellerDesignerPageModelService {



    /**
     * 保存设计器页面
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/onlySaveOne",produces = "application/json;charset=UTF-8")
    ResultObjectVO onlySaveOne(@RequestBody RequestJsonVO requestJsonVO);




}
