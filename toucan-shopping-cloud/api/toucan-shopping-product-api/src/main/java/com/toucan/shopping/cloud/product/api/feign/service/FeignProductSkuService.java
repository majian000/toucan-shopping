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
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryList(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 根据ID查询(商城PC端使用)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id/for/front",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByIdForFront(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 修改库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(method= RequestMethod.POST,value="/update/stock",produces = "application/json;charset=UTF-8")
    ResultObjectVO updateStock(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 修改单价
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(method= RequestMethod.POST,value="/update/price",produces = "application/json;charset=UTF-8")
    ResultObjectVO updatePrice(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询,只查询1个sku(商城PC端预览使用)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/one/by/shop/product/id/for/front/preview",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryOneByShopProductIdForFrontPreview(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 根据ID查询(商城PC端预览使用)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id/for/front/preview",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByIdForFrontPreview(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询,只查询1个sku(商城PC端使用)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/one/by/shop/product/id/for/front",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryOneByShopProductIdForFront(@RequestBody RequestJsonVO requestJsonVO);




    /**
     * 商品上架/下架
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/shelves",produces = "application/json;charset=UTF-8")
    ResultObjectVO shelves(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 扣库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(method= RequestMethod.POST,value="/inventoryReduction",produces = "application/json;charset=UTF-8")
    ResultObjectVO inventoryReduction(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 恢复扣库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(method= RequestMethod.POST,value="/restoreStock",produces = "application/json;charset=UTF-8")
    ResultObjectVO restoreStock(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 修改商品预览图
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/preview/photo",produces = "application/json;charset=UTF-8")
    ResultObjectVO updatePreviewPhoto(@RequestBody RequestJsonVO requestJsonVO);




    /**
     * 修改商品介绍图
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/description/photo",produces = "application/json;charset=UTF-8")
    ResultObjectVO updateDescriptionPhoto(@RequestBody RequestJsonVO requestJsonVO);




    /**
     * 移除商品介绍图
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/remove/description/photo",produces = "application/json;charset=UTF-8")
    ResultObjectVO removeDescriptionPhoto(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 根据店铺商品ID查询SKU列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/shopProductIdList",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListByShopProductIdList(@RequestBody RequestJsonVO requestJsonVO);
}
