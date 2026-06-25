package com.toucan.shopping.cloud.product.api.cloud.feign.service;

import com.toucan.shopping.cloud.product.api.ShopProductServiceAPI;
import com.toucan.shopping.cloud.product.api.cloud.feign.fallback.FeignShopProductServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-product-proxy/shopProduct",fallbackFactory = FeignShopProductServiceFallbackFactory.class)
public interface FeignShopProductService extends ShopProductServiceAPI {


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value="/query/list/by/shop/product/uuid",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListByShopProductUuid(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/query/list",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryList(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/query/id",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByShopProductId(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 商品上架/下架
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/shelves",produces = "application/json;charset=UTF-8")
    ResultObjectVO shelves(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据运费模板ID查询关联的商品
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/query/one/by/freightTemplateId",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryOneByFreightTemplateId(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 修改运费模板
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/update/freightTemplate",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO updateFreightTemplate(@RequestBody RequestJsonVO requestJsonVO);

}
