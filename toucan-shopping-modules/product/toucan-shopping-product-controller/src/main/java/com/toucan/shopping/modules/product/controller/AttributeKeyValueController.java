package com.toucan.shopping.modules.product.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.business.service.AttributeKeyValueBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 属性键值管理
 * @author majian
 */
@RestController
@RequestMapping("/attributeKeyValue")
public class AttributeKeyValueController {

    @Autowired
    private AttributeKeyValueBusinessService attributeKeyValueBusinessService;


    /**
     * 根据分类ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/category/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findByCategoryId(@RequestBody RequestJsonVO requestVo){
        return attributeKeyValueBusinessService.findByCategoryId(requestVo);
    }




    /**
     * 根据分类ID查询属性树
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/query/attribute/tree/page", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAttributeTreePage(@RequestBody RequestJsonVO requestVo){
        return attributeKeyValueBusinessService.queryAttributeTreePage(requestVo);
    }




    /**
     * 根据SPUID和SKU中的分类ID以及属性 查询可被搜索的属性列表
     * 注:同一级分类下属性名称不允许重复
     * 例如：手机分类下有颜色属性，游戏手机分类下也允许有颜色属性
     * 结构:
     *      手机 颜色属性
     *      手机》游戏手机 颜色属性
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/query/search/attribute/list/spuId/categoryId/attributeName/AttributeValue", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO querySearchAttributeList(@RequestBody RequestJsonVO requestVo){
        return attributeKeyValueBusinessService.querySearchAttributeList(requestVo);
    }

}
