package com.toucan.shopping.modules.seller.controller;


import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.business.service.SellerShopBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺管理 增删改查
 */
@RestController
@RequestMapping("/sellerShop")
public class SellerShopController {

    @Autowired
    private SellerShopBusinessService sellerShopBusinessService;


    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO){
        return sellerShopBusinessService.save(requestJsonVO);
    }


    /**
     * 查询指定用户的店铺
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/by/user",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findByUser(@RequestBody RequestJsonVO requestVo){
        return sellerShopBusinessService.findByUser(requestVo);
    }




    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        return sellerShopBusinessService.findById(requestVo);
    }



    /**
     * 根据ID集合查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/idList",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findByIdList(@RequestBody RequestJsonVO requestVo){
        return sellerShopBusinessService.findByIdList(requestVo);
    }


    /**
     * 刷新缓存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/flushCache",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO flushCache(@RequestBody RequestJsonVO requestJsonVO){
        return sellerShopBusinessService.flushCache(requestJsonVO);
    }


    /**
     * 更新
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO)
    {
        return sellerShopBusinessService.update(requestJsonVO);
    }





    /**
     * 更新图标
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/logo",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateLogo(@RequestBody RequestJsonVO requestJsonVO)
    {
        return sellerShopBusinessService.updateLogo(requestJsonVO);
    }





    /**
     * 更新介绍
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/info",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateInfo(@RequestBody RequestJsonVO requestJsonVO)
    {
        return sellerShopBusinessService.updateInfo(requestJsonVO);
    }

    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO)
    {
        return sellerShopBusinessService.deleteById(requestJsonVO);
    }





    /**
     * 批量删除功能项
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        return sellerShopBusinessService.deleteByIds(requestVo);
    }



    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo){
        return sellerShopBusinessService.queryListPage(requestVo);
    }





    /**
     * 禁用启用店铺
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/disabled/enabled",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO disabledEnabled(@RequestBody RequestJsonVO requestVo){
        return sellerShopBusinessService.disabledEnabled(requestVo);
    }


}
