package com.toucan.shopping.modules.seller.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.business.service.SellerDesignerImageBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 店铺图片
 */
@RestController
@RequestMapping("/seller/designer/image")
public class SellerDesignerImageController {

    @Autowired
    private SellerDesignerImageBusinessService sellerDesignerImageBusinessService;


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo) {
        return sellerDesignerImageBusinessService.findById(requestVo);
    }


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO) {
        return sellerDesignerImageBusinessService.queryListPage(requestJsonVO);
    }


    /**
     * 保存轮播图
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO) {
        return sellerDesignerImageBusinessService.save(requestJsonVO);
    }


    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO) {
        return sellerDesignerImageBusinessService.deleteById(requestJsonVO);
    }


    /**
     * 修改装修图片
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO) {
        return sellerDesignerImageBusinessService.update(requestJsonVO);
    }


    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIdForAdmin(@RequestBody RequestJsonVO requestJsonVO) {
        return sellerDesignerImageBusinessService.deleteByIdForAdmin(requestJsonVO);
    }

}
