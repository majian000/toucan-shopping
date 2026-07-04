package com.toucan.shopping.modules.category.controller;

import com.toucan.shopping.modules.category.business.service.CategoryHotBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 热门类别控制器
 */
@RestController
@RequestMapping("/category/hot")
public class CategoryHotController {

    @Autowired
    private CategoryHotBusinessService categoryHotBusinessService;


    /**
     * 保存类别
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO)
    {
        return categoryHotBusinessService.save(requestJsonVO);
    }



    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table/by/pid", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeTableByPid(@RequestBody RequestJsonVO requestJsonVO){
        return categoryHotBusinessService.queryTreeTableByPid(requestJsonVO);
    }





}
