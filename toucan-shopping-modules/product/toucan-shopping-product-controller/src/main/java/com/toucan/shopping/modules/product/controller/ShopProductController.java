package com.toucan.shopping.modules.product.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.business.service.ShopProductBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺商品
 * @auth majian
 */
@RestController
@RequestMapping("/shopProduct")
public class ShopProductController {

    @Autowired
    private ShopProductBusinessService shopProductBusinessService;


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductBusinessService.queryListPage(requestJsonVO);
    }


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryList(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductBusinessService.queryList(requestJsonVO);
    }

    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryByShopProductId(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductBusinessService.queryByShopProductId(requestJsonVO);
    }


    /**
     * 商品上架/下架
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/shelves", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO shelves(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductBusinessService.shelves(requestJsonVO);
    }


    /**
     * 根据运费模板ID查询关联的商品
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/one/by/freightTemplateId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryOneByFreightTemplateId(@RequestBody RequestJsonVO requestJsonVO) {
        return shopProductBusinessService.queryOneByFreightTemplateId(requestJsonVO);
    }

    /**
     * 根据shop_product_uuid查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/shop/product/uuid", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryListByShopProductUuid(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductBusinessService.queryListByShopProductUuid(requestJsonVO);
    }


    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductBusinessService.deleteById(requestJsonVO);
    }


    /**
     * 修改运费模板
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/freightTemplate", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO updateFreightTemplate(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductBusinessService.updateFreightTemplate(requestJsonVO);
    }

}
