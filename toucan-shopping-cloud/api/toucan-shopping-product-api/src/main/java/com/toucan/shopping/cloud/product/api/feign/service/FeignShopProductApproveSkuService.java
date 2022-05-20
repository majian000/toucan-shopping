package com.toucan.shopping.cloud.product.api.feign.service;

import com.toucan.shopping.cloud.product.api.feign.fallback.FeignProductSkuServiceFallbackFactory;
import com.toucan.shopping.cloud.product.api.feign.fallback.FeignShopProductApproveSkuServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultListVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-product-proxy/shopProductApproveSku",fallbackFactory = FeignShopProductApproveSkuServiceFallbackFactory.class)
public interface FeignShopProductApproveSkuService {


    @RequestMapping(value = "/query/id",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryById(@RequestBody RequestJsonVO requestJsonVO);


    @RequestMapping(value = "/query/ids",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByIdList(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询(商城PC端使用)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id/for/front",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByIdForFront(@RequestBody RequestJsonVO requestJsonVO);

}
