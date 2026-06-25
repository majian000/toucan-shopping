package com.toucan.shopping.modules.category.controller;

import com.toucan.shopping.modules.category.business.service.CategoryBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 类别控制器
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryBusinessService categoryBusinessService;


    /**
     * 保存类别
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestHeader(value = "toucan-sign-header",defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO)
    {
        return categoryBusinessService.save(signHeader, requestJsonVO);
    }



    /**
     * 更新类别
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO)
    {
        return categoryBusinessService.update(requestJsonVO);
    }




    /**
     * 根据ID删除类别
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id",method = RequestMethod.DELETE,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO deleteById(@RequestHeader(value = "toucan-sign-header",defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO)
    {
        return categoryBusinessService.deleteById(signHeader, requestJsonVO);
    }

    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryById(@RequestBody RequestJsonVO requestJsonVO)
    {
        return categoryBusinessService.queryById(requestJsonVO);
    }


    /**
     * 根据ID数组查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/ids",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryByIdList(@RequestBody RequestJsonVO requestJsonVO)
    {
        return categoryBusinessService.queryByIdList(requestJsonVO);
    }


    /**
     * 刷新全部缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/flush/all/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO flushAllCache(@RequestBody RequestJsonVO requestVo){
        return categoryBusinessService.flushAllCache(requestVo);
    }


    /**
     * 刷新首页缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/flush/index/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO flushWebIndexCache(@RequestBody RequestJsonVO requestVo){
        return categoryBusinessService.flushWebIndexCache(requestVo);
    }



    /**
     * 刷新预览树缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/flush/wmini/tree",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO flushWMiniTreeCache(@RequestBody RequestJsonVO requestVo){
        return categoryBusinessService.flushWMiniTreeCache(requestVo);
    }


    /**
     * 导航分类树
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/flush/navigation/mini/tree",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO flushNavigationMiniTreeCache(@RequestBody RequestJsonVO requestVo){
        return categoryBusinessService.flushNavigationMiniTreeCache(requestVo);
    }

    /**
     * 清空首页缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/clear/index/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO clearWebIndexCache(@RequestBody RequestJsonVO requestVo){
        return categoryBusinessService.clearWebIndexCache(requestVo);
    }





    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        return categoryBusinessService.findById(requestVo);
    }





    /**
     * 根据ID查询返回分类ID路径
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/path/by/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findIdPathById(@RequestBody RequestJsonVO requestVo){
        return categoryBusinessService.findIdPathById(requestVo);
    }


    /**
     * 根据ID数组查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/idArray",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findByIdArray(@RequestBody RequestJsonVO requestVo){
        return categoryBusinessService.findByIdArray(requestVo);
    }


    /**
     * 查询树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        return categoryBusinessService.queryTree(requestJsonVO);
    }


    /**
     * 查询迷你树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/tree/mini",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryMiniTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        return categoryBusinessService.queryMiniTree(requestJsonVO);
    }



    /**
     * 查询PC端首页类别树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/web/index/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryWebIndexTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        return categoryBusinessService.queryWebIndexTree(requestJsonVO);
    }



    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeTable(@RequestBody RequestJsonVO requestJsonVO){
        return categoryBusinessService.queryTreeTable(requestJsonVO);
    }



    /**
     * 查询指定节点下所有子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryListByPid(@RequestBody RequestJsonVO requestJsonVO){
        return categoryBusinessService.queryListByPid(requestJsonVO);
    }





    /**
     * 查询指定节点下所有子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/child/list/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryChildListByPid(@RequestBody RequestJsonVO requestJsonVO){
        return categoryBusinessService.queryChildListByPid(requestJsonVO);
    }

    /**
     * 查询指定节点下一级子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/next/one/level/child/list/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryNextOneLevelChildListByPid(@RequestBody RequestJsonVO requestJsonVO){
        return categoryBusinessService.queryNextOneLevelChildListByPid(requestJsonVO);
    }

    /**
     * 查询树表格byPid
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeTableByPid(@RequestBody RequestJsonVO requestJsonVO){
        return categoryBusinessService.queryTreeTableByPid(requestJsonVO);
    }





    /**
     * 批量删除分类
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        return categoryBusinessService.deleteByIds(requestVo);
    }





    /**
     * 查询类别树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/category/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryCategoryTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        return categoryBusinessService.queryCategoryTree(requestJsonVO);
    }





    /**
     * 查询指定节点下子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/child",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeChildByPid(@RequestBody RequestJsonVO requestJsonVO){
        return categoryBusinessService.queryTreeChildByPid(requestJsonVO);
    }



    /**
     * 查询全部
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/all/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryAllList(@RequestBody RequestJsonVO requestJsonVO)
    {
        return categoryBusinessService.queryAllList(requestJsonVO);
    }



}
