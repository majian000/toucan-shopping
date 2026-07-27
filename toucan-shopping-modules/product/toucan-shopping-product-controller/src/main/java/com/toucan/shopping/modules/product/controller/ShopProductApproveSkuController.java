package com.toucan.shopping.modules.product.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.business.service.ShopProductApproveSkuBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopProductApproveSku")
public class ShopProductApproveSkuController {

    @Autowired
    private ShopProductApproveSkuBusinessService shopProductApproveSkuBusinessService;

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductApproveSkuBusinessService.queryListPage(requestJsonVO);
    }


    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryById(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductApproveSkuBusinessService.queryById(requestJsonVO);
    }


    /**
     * 根据ID查询(商城PC端使用)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id/for/front", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryByIdForFront(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductApproveSkuBusinessService.queryByIdForFront(requestJsonVO);
    }


    /**
     * 根据ID查询,只查询1个sku(商城PC端使用)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/one/by/productApproveId/for/front", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryOneByProductApproveIdForFront(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductApproveSkuBusinessService.queryOneByProductApproveIdForFront(requestJsonVO);
    }


    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/ids", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryByIdList(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductApproveSkuBusinessService.queryByIdList(requestJsonVO);
    }


}
