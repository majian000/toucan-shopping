package com.toucan.shopping.modules.product.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultListVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.product.business.service.ProductSkuBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productSku")
public class ProductSkuController {

    @Autowired
    private ProductSkuBusinessService productSkuBusinessService;

    /**
     * 查询所有上架商品
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/shelves/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultListVO queryShelvesList(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuBusinessService.queryShelvesList(requestJsonVO);
    }


    /**
     * 根据ID查询(商城PC端使用)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id/for/front", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryByIdForFront(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuBusinessService.queryByIdForFront(requestJsonVO);
    }


    /**
     * 根据ID查询(商城PC端预览使用)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id/for/front/preview", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryByIdForFrontPreview(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuBusinessService.queryByIdForFrontPreview(requestJsonVO);
    }

    /**
     * 根据ID查询,只查询1个sku(商城PC端使用)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/one/by/shop/product/id/for/front", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryOneByShopProductIdForFront(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuBusinessService.queryOneByShopProductIdForFront(requestJsonVO);
    }


    /**
     * 根据ID查询,只查询1个sku(商城PC端预览使用)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/one/by/shop/product/id/for/front/preview", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryOneByShopProductIdForFrontPreview(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuBusinessService.queryOneByShopProductIdForFrontPreview(requestJsonVO);
    }

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuBusinessService.queryListPage(requestJsonVO);
    }


    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryById(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuBusinessService.queryById(requestJsonVO);
    }


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryList(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuBusinessService.queryList(requestJsonVO);
    }


    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/ids", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryByIdList(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuBusinessService.queryByIdList(requestJsonVO);
    }


    /**
     * 扣库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/inventoryReduction", produces = "application/json;charset=UTF-8")
    public ResultObjectVO inventoryReduction(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuBusinessService.inventoryReduction(requestJsonVO);
    }


    /**
     * 修改库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/update/stock", produces = "application/json;charset=UTF-8")
    public ResultObjectVO updateStock(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuBusinessService.updateStock(requestJsonVO);
    }

    /**
     * 修改单价
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/update/price", produces = "application/json;charset=UTF-8")
    public ResultObjectVO updatePrice(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuBusinessService.updatePrice(requestJsonVO);
    }


    /**
     * 恢复扣库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/restoreStock", produces = "application/json;charset=UTF-8")
    public ResultObjectVO restoreStock(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuBusinessService.restoreStock(requestJsonVO);
    }


    /**
     * 商品上架/下架
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/shelves", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO shelves(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuBusinessService.shelves(requestJsonVO);
    }


    /**
     * 修改商品预览图
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/preview/photo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO updatePreviewPhoto(@RequestBody RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.updatePreviewPhoto(requestJsonVO);
    }


    /**
     * 修改商品介绍图
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/description/photo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO updateDescriptionPhoto(@RequestBody RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.updateDescriptionPhoto(requestJsonVO);
    }


    /**
     * 移除商品介绍图
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/remove/description/photo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO removeDescriptionPhoto(@RequestBody RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.removeDescriptionPhoto(requestJsonVO);
    }


    /**
     * 根据店铺商品ID查询SKU列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/shopProductIdList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryListByShopProductIdList(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuBusinessService.queryListByShopProductIdList(requestJsonVO);
    }


    /**
     * 根据店铺ID查询上架商品数量
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/shelves/count/by/shopId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultTypeObjectVO<Long> queryShelvesCountByShopId(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuBusinessService.queryShelvesCountByShopId(requestJsonVO);
    }
}
