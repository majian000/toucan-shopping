package com.toucan.shopping.cloud.product.api.feign.service;

import com.toucan.shopping.cloud.product.api.feign.fallback.FeignShopProductApproveServiceFallbackFactory;
import com.toucan.shopping.cloud.product.api.feign.fallback.FeignShopProductServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.page.ShopProductApprovePageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-product-proxy/shopProductApprove",fallbackFactory = FeignShopProductApproveServiceFallbackFactory.class)
public interface FeignShopProductApproveService {


    /**
     * 发布商品
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/publish",produces = "application/json;charset=UTF-8")
    ResultObjectVO publish(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 发布商品
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/republish",produces = "application/json;charset=UTF-8")
    ResultObjectVO republish(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);




    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByProductApproveId(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据ID和店铺ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id/shopId",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByProductApproveIdAndShopId(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 审核驳回
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/reject",produces = "application/json;charset=UTF-8")
    ResultObjectVO reject(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 审核通过
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/pass",produces = "application/json;charset=UTF-8")
    ResultObjectVO pass(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据ID和店铺ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id/shopId",produces = "application/json;charset=UTF-8")
    ResultObjectVO deleteByProductApproveIdAndShopId(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 查询这个店铺最新发布的那几条审核
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/newest/list/shopId",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryNewestListByShopId(@RequestBody RequestJsonVO requestJsonVO);

}
