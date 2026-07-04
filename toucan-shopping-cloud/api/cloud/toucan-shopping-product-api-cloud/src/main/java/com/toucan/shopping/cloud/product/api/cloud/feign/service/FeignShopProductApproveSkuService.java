package com.toucan.shopping.cloud.product.api.cloud.feign.service;

import com.toucan.shopping.cloud.product.api.ShopProductApproveSkuServiceAPI;
import com.toucan.shopping.cloud.product.api.cloud.feign.fallback.FeignShopProductApproveSkuServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-product-proxy/shopProductApproveSku",fallbackFactory = FeignShopProductApproveSkuServiceFallbackFactory.class)
public interface FeignShopProductApproveSkuService extends ShopProductApproveSkuServiceAPI {


    @Override
    @RequestMapping(value = "/query/id",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryById(@RequestBody RequestJsonVO requestJsonVO);


    @Override
    @RequestMapping(value = "/query/ids",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByIdList(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询(商城PC端使用)
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/query/id/for/front", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByIdForFront(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据ID查询,只查询1个sku(商城PC端使用)
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/query/one/by/productApproveId/for/front", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryOneByProductApproveIdForFront(@RequestBody RequestJsonVO requestJsonVO);


}
