package com.toucan.shopping.modules.product.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.business.service.AttributeKeyBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 属性键管理
 * @author majian
 */
@RestController
@RequestMapping("/attributeKey")
public class AttributeKeyController {

    @Autowired
    private AttributeKeyBusinessService attributeKeyBusinessService;


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        return attributeKeyBusinessService.queryListPage(requestJsonVO);
    }


    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table/by/pid", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO queryTreeTableByPid(@RequestBody RequestJsonVO requestJsonVO){
        return attributeKeyBusinessService.queryTreeTableByPid(requestJsonVO);
    }


    /**
     * 查询树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/tree/category/id",method = RequestMethod.POST)
    public ResultObjectVO queryTreeByCategoryId(@RequestBody RequestJsonVO requestJsonVO)
    {
        return attributeKeyBusinessService.queryTreeByCategoryId(requestJsonVO);
    }


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        return attributeKeyBusinessService.findById(requestVo);
    }


    /**
     * 保存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO)
    {
        return attributeKeyBusinessService.save(requestJsonVO);
    }


    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        return attributeKeyBusinessService.deleteById(requestVo);
    }


    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        return attributeKeyBusinessService.deleteByIds(requestVo);
    }


    /**
     * 編輯
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        return attributeKeyBusinessService.update(requestVo);
    }


    /**
     * 查询所有可搜索的属性键值对
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/search/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO querySearchList(@RequestBody RequestJsonVO requestJsonVO)
    {
        return attributeKeyBusinessService.querySearchList(requestJsonVO);
    }

}
