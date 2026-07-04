package com.toucan.shopping.modules.color.table.controller;

import com.toucan.shopping.modules.color.table.business.service.ColorTableBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 颜色表操作
 */
@RestController
@RequestMapping("/colorTable")
public class ColorTableController {

    @Autowired
    private ColorTableBusinessService colorTableBusinessService;


    /**
     * 保存颜色表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO)
    {
        return colorTableBusinessService.save(requestJsonVO);
    }





    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        return colorTableBusinessService.findById(requestVo);
    }



    /**
     * 编辑
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        return colorTableBusinessService.update(requestVo);
    }



    /**
     * 查询列表分页
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        return colorTableBusinessService.queryListPage(requestJsonVO);
    }



    /**
     * 根据名称集合查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/names", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListByNames(@RequestBody RequestJsonVO requestJsonVO)
    {
        return colorTableBusinessService.queryListByNames(requestJsonVO);
    }




    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryList(@RequestBody RequestJsonVO requestJsonVO)
    {
        return colorTableBusinessService.queryList(requestJsonVO);
    }



    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        return colorTableBusinessService.deleteById(requestVo);
    }


    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        return colorTableBusinessService.deleteByIds(requestVo);
    }



}
