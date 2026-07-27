package com.toucan.shopping.modules.seller.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.business.service.ShopCategoryBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 店铺分类控制器
 */
@RestController
@RequestMapping("/shop/category")
public class ShopCategoryController {

    @Autowired
    private ShopCategoryBusinessService shopCategoryBusinessService;


    /**
     * 保存分类
     */
    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO save(@RequestHeader(value = "toucan-sign-header",defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO)
    {
        return shopCategoryBusinessService.save(requestJsonVO);
    }


    /**
     * 保存分类(后台管理端)
     */
    @RequestMapping(value="/admin/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO saveForAdmin(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopCategoryBusinessService.saveForAdmin(requestJsonVO);
    }


    /**
     * 更新分类
     */
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopCategoryBusinessService.update(requestJsonVO);
    }



    /**
     * 更新分类(后台管理端)
     */
    @RequestMapping(value="/admin/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO updateForAdmin(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopCategoryBusinessService.updateForAdmin(requestJsonVO);
    }





    /**
     * 置顶
     */
    @RequestMapping(value="/move/top", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO moveTop(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopCategoryBusinessService.moveTop(requestJsonVO);
    }



    /**
     * 置顶(后台管理端)
     */
    @RequestMapping(value="/admin/move/top", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO moveTopForAdmin(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopCategoryBusinessService.moveTopForAdmin(requestJsonVO);
    }

    /**
     * 置底
     */
    @RequestMapping(value="/move/bottom", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO moveBottom(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopCategoryBusinessService.moveBottom(requestJsonVO);
    }



    /**
     * 置底(后台管理端)
     */
    @RequestMapping(value="/admin/move/bottom", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO moveBottomForAdmin(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopCategoryBusinessService.moveBottomForAdmin(requestJsonVO);
    }


    /**
     * 向上
     */
    @RequestMapping(value="/move/up", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO moveUp(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopCategoryBusinessService.moveUp(requestJsonVO);
    }




    /**
     * 向上(后台管理端)
     */
    @RequestMapping(value="/admin/move/up", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO moveUpForAdmin(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopCategoryBusinessService.moveUpForAdmin(requestJsonVO);
    }


    /**
     * 向下
     */
    @RequestMapping(value="/move/down", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO moveDown(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopCategoryBusinessService.moveDown(requestJsonVO);
    }




    /**
     * 向下(后台管理端)
     */
    @RequestMapping(value="/admin/move/down", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO moveDownForAdmin(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopCategoryBusinessService.moveDownForAdmin(requestJsonVO);
    }

    /**
     * 根据ID查询
     */
    @RequestMapping(value="/query/id", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryById(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopCategoryBusinessService.queryById(requestJsonVO);
    }


    /**
     * 根据ID查询
     */
    @RequestMapping(value="/query/ids", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryByIdList(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO)
    {
        return shopCategoryBusinessService.queryByIdList(requestJsonVO);
    }



    /**
     * 批量刷新缓存
     */
    @RequestMapping(value="/flush/cache", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO flushCache(@RequestBody RequestJsonVO requestVo){
        return shopCategoryBusinessService.flushCache(requestVo);
    }


    /**
     * 清空缓存
     */
    @RequestMapping(value="/clear/cache", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO clearCache(@RequestBody RequestJsonVO requestVo){
        return shopCategoryBusinessService.clearCache(requestVo);
    }




    /**
     * 根据ID查询
     */
    @RequestMapping(value="/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        return shopCategoryBusinessService.findById(requestVo);
    }




    /**
     * 根据ID查询返回分类ID路径
     */
    @RequestMapping(value="/find/path/by/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO findIdPathById(@RequestBody RequestJsonVO requestVo)
    {
        return shopCategoryBusinessService.findIdPathById(requestVo);
    }


    /**
     * 根据ID数组查询
     */
    @RequestMapping(value="/find/idArray", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO findByIdArray(@RequestBody RequestJsonVO requestVo){
        return shopCategoryBusinessService.findByIdArray(requestVo);
    }


    /**
     * 查询树
     */
    @RequestMapping(value = "/query/tree",method = RequestMethod.POST)
    public ResultObjectVO queryTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopCategoryBusinessService.queryTree(requestJsonVO);
    }



    /**
     * 查询PC端首页分类树
     */
    @RequestMapping(value = "/query/web/index/tree",method = RequestMethod.POST)
    public ResultObjectVO queryWebIndexTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopCategoryBusinessService.queryWebIndexTree(requestJsonVO);
    }



    /**
     * 查询树表格
     */
    @RequestMapping(value="/query/tree/table", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO queryTreeTable(@RequestBody RequestJsonVO requestJsonVO){
        return shopCategoryBusinessService.queryTreeTable(requestJsonVO);
    }



    /**
     * 查询指定节点下所有子节点
     */
    @RequestMapping(value="/query/list/by/pid", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO queryListByPid(@RequestBody RequestJsonVO requestJsonVO){
        return shopCategoryBusinessService.queryListByPid(requestJsonVO);
    }


    /**
     * 查询全部类别
     */
    @RequestMapping(value="/query/all/list", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO queryAllList(@RequestBody RequestJsonVO requestJsonVO){
        return shopCategoryBusinessService.queryAllList(requestJsonVO);
    }



    /**
     * 根据店铺ID查询所有分类
     */
    @RequestMapping(value="/query/list/by/shopId", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO queryListByShopId(@RequestBody RequestJsonVO requestJsonVO){
        return shopCategoryBusinessService.queryListByShopId(requestJsonVO);
    }

    /**
     * 查询树表格
     */
    @RequestMapping(value="/query/tree/table/by/pid", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO queryTreeTableByPid(@RequestBody RequestJsonVO requestJsonVO){
        return shopCategoryBusinessService.queryTreeTableByPid(requestJsonVO);
    }



    /**
     * 根据ID删除
     */
    @RequestMapping(value="/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopCategoryBusinessService.deleteById(requestJsonVO);
    }


    /**
     * 根据ID删除(后台管理)
     */
    @RequestMapping(value="/admin/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO deleteByIdForAdmin(@RequestBody RequestJsonVO requestJsonVO)
    {
        return shopCategoryBusinessService.deleteByIdForAdmin(requestJsonVO);
    }


    /**
     * 批量删除
     */
    @RequestMapping(value="/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        return shopCategoryBusinessService.deleteByIds(requestVo);
    }



}
