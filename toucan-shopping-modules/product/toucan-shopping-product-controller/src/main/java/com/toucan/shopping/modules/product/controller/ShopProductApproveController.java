package com.toucan.shopping.modules.product.controller;

import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.business.service.ShopProductApproveBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 店铺商品审核
 * @auth majian
 */
@RestController
@RequestMapping("/shopProductApprove")
public class ShopProductApproveController {

    @Autowired
    private ShopProductApproveBusinessService shopProductApproveBusinessService;


    /**
     * 发布商品
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/publish", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO publish(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductApproveBusinessService.publish(requestJsonVO);
    }



    /**
     * 重新发布商品
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/republish", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO republish(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductApproveBusinessService.republish(requestJsonVO);
    }




    /**
     * 根据店铺ID查询所有审核中
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/approve/list/shopId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryApproveListByShopId(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductApproveBusinessService.queryApproveListByShopId(requestJsonVO);
    }


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductApproveBusinessService.queryListPage(requestJsonVO);
    }



    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryByProductApproveId(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductApproveBusinessService.queryByProductApproveId(requestJsonVO);
    }



    /**
     * 根据ID和店铺ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id/shopId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryByProductApproveIdAndShopId(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductApproveBusinessService.queryByProductApproveIdAndShopId(requestJsonVO);
    }





    /**
     * 查询这个店铺最新发布的那几条审核
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/newest/list/shopId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryNewestListByShopId(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductApproveBusinessService.queryNewestListByShopId(requestJsonVO);
    }





    /**
     * 根据ID和店铺ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id/shopId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO deleteByProductApproveIdAndShopId(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductApproveBusinessService.deleteByProductApproveIdAndShopId(requestJsonVO);
    }

    /**
     * 审核驳回
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/reject", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO reject(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductApproveBusinessService.reject(requestJsonVO);
    }





    /**
     * 审核通过
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/pass", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO pass(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductApproveBusinessService.pass(requestJsonVO);
    }


    /**
     * 根据运费模板ID查询审核中的信息(一条)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/one/underReview/by/freightTemplateId", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findOneUnderReviewByFreightTemplateId(@RequestBody RequestJsonVO requestJsonVO) {
        return shopProductApproveBusinessService.findOneUnderReviewByFreightTemplateId(requestJsonVO);
    }

    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductApproveBusinessService.deleteById(requestJsonVO);
    }




    /**
     * 查询审核中数量
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/approve/count/shopId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultTypeObjectVO<Long> queryApproveCountByShopId(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopProductApproveBusinessService.queryApproveCountByShopId(requestJsonVO);
    }

}
