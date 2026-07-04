package com.toucan.shopping.modules.seller.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.business.service.ShopBannerBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 轮播图操作
 */
@RestController
@RequestMapping("/shop/banner")
public class ShopBannerController {

    @Autowired
    private ShopBannerBusinessService shopBannerBusinessService;

    /**
     * 保存轮播图
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO) {
        return shopBannerBusinessService.save(requestJsonVO);
    }


    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO) {
        return shopBannerBusinessService.deleteById(requestJsonVO);
    }


    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIdForAdmin(@RequestBody RequestJsonVO requestJsonVO) {
        return shopBannerBusinessService.deleteByIdForAdmin(requestJsonVO);
    }


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO) {
        return shopBannerBusinessService.queryListPage(requestJsonVO);
    }


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo) {
        return shopBannerBusinessService.findById(requestVo);
    }


    /**
     * 修改轮播图
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO) {
        return shopBannerBusinessService.update(requestJsonVO);
    }


    /**
     * 查询首页列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryIndexList", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryIndexList(@RequestBody RequestJsonVO requestVo) {
        return shopBannerBusinessService.queryIndexList(requestVo);
    }
}
