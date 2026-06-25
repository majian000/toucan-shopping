package com.toucan.shopping.modules.area.controller;

import com.toucan.shopping.modules.area.business.service.AreaBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 管理端地区操作
 */
@RestController
@RequestMapping("/area")
public class AreaController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AreaBusinessService areaBusinessService;


    /**
     * 保存地区编码
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestHeader(value = "toucan-sign-header",defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO)
    {
        return areaBusinessService.save(requestJsonVO);
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
        return areaBusinessService.deleteById(requestJsonVO);
    }

    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryById(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO)
    {
        return areaBusinessService.queryById(requestJsonVO);
    }

    /**
     * 批量删除功能项
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        return areaBusinessService.deleteByIds(requestVo);
    }

    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/ids",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryByIdList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO)
    {
        return areaBusinessService.queryByIdList(requestJsonVO);
    }

    /**
     * 查询指定应用下地区树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/all",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryAll(@RequestBody RequestJsonVO requestJsonVO)
    {
        return areaBusinessService.queryAll(requestJsonVO);
    }

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        return areaBusinessService.findById(requestVo);
    }

    /**
     * 根据编码查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/codes",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findByCodes(@RequestBody RequestJsonVO requestVo){
        return areaBusinessService.findByCodes(requestVo);
    }

    /**
     * 編輯
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        return areaBusinessService.update(requestVo);
    }

    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeTable(@RequestBody RequestJsonVO requestJsonVO){
        return areaBusinessService.queryTreeTable(requestJsonVO);
    }

    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeTableByPid(@RequestBody RequestJsonVO requestJsonVO){
        return areaBusinessService.queryTreeTableByPid(requestJsonVO);
    }

    /**
     * 查询指定节点下所有子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryListByPid(@RequestBody RequestJsonVO requestJsonVO){
        return areaBusinessService.queryListByPid(requestJsonVO);
    }

    /**
     * 查询指定节点下子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/child",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeChildByPid(@RequestBody RequestJsonVO requestJsonVO){
        return areaBusinessService.queryTreeChildByPid(requestJsonVO);
    }

    /**
     * 查询指定节点下所有子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/parentCode",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryListByParentCode(@RequestBody RequestJsonVO requestJsonVO){
        return areaBusinessService.queryListByParentCode(requestJsonVO);
    }

    /**
     * 查询全量缓存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/full/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryFullCache(@RequestBody RequestJsonVO requestJsonVO){
        return areaBusinessService.queryFullCache(requestJsonVO);
    }

    /**
     * 刷新全部缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/flush/all/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO flushAllCache(@RequestBody RequestJsonVO requestVo){
        return areaBusinessService.flushAllCache(requestVo);
    }

    /**
     * 查询地区树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        return areaBusinessService.queryTree(requestJsonVO);
    }

    /**
     * 根据所有市级名称查询出所有市级对象
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/city/list/by/names",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryCityListByNames(@RequestBody RequestJsonVO requestJsonVO){
        return areaBusinessService.queryCityListByNames(requestJsonVO);
    }

}
