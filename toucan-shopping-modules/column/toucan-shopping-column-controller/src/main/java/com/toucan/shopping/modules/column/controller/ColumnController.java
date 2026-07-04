package com.toucan.shopping.modules.column.controller;


import com.toucan.shopping.modules.column.business.service.ColumnBusinessService;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 栏目控制器
 * @author majian
 */
@RestController
@RequestMapping("/column")
public class ColumnController {

    @Autowired
    private ColumnBusinessService columnBusinessService;

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        return columnBusinessService.queryListPage(requestJsonVO);
    }



    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO){
        return columnBusinessService.update(requestJsonVO);
    }





    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO){
        return columnBusinessService.save(requestJsonVO);
    }




    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table/by/pid", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeTableByPid(@RequestBody RequestJsonVO requestJsonVO){
        return columnBusinessService.queryTreeTableByPid(requestJsonVO);
    }


    /**
     * 查询栏目树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/column/tree/pid",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryColumnTreeByPid(@RequestBody RequestJsonVO requestJsonVO)
    {
        return columnBusinessService.queryColumnTreeByPid(requestJsonVO);
    }



    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultTypeObjectVO<ColumnVO> findById(@RequestBody RequestJsonVO requestVo){
        return columnBusinessService.findById(requestVo);
    }


    /**
     * 删除指定栏目
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        return columnBusinessService.deleteById(requestVo);
    }


    /**
     * 批量删除栏目
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        return columnBusinessService.deleteByIds(requestVo);
    }



    /**
     * 查询指定节点下所有子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/pid", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryListByPid(@RequestBody RequestJsonVO requestJsonVO){
        return columnBusinessService.queryListByPid(requestJsonVO);
    }


}
