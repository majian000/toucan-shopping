package com.toucan.shopping.cloud.product.api.feign.service;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultListVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.cloud.product.api.feign.fallback.FeignProductSkuServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-product-proxy/productSku",fallbackFactory = FeignProductSkuServiceFallbackFactory.class)
public interface FeignProductSkuService {

    @RequestMapping(value = "/shelves/list",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultListVO queryShelvesList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody  RequestJsonVO requestJsonVO);


    @RequestMapping(value = "/query/id",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryById(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/ids",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByIdList(@RequestHeader( value = "toucan-sign-header",defaultValue = "-1") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

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


    /**
     * 根据ID查询,只查询1个sku(商城PC端使用)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/one/by/shop/product/id/for/front",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryOneByShopProductIdForFront(@RequestBody RequestJsonVO requestJsonVO);




    /**
     * 扣库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(method= RequestMethod.POST,value="/inventoryReduction",produces = "application/json;charset=UTF-8")
    ResultObjectVO inventoryReduction(@RequestBody RequestJsonVO requestJsonVO);
}
