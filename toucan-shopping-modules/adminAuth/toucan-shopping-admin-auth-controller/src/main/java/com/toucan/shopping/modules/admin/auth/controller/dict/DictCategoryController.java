package com.toucan.shopping.modules.admin.auth.controller.dict;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.business.service.DictCategoryBusinessService;
import com.toucan.shopping.modules.admin.auth.entity.Dict;
import com.toucan.shopping.modules.admin.auth.entity.DictCategory;
import com.toucan.shopping.modules.admin.auth.page.DictCategoryPageInfo;
import com.toucan.shopping.modules.admin.auth.service.*;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.admin.auth.vo.DictCategoryVO;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 字典分类
 */
@RestController
@RequestMapping("/dictCategory")
public class DictCategoryController {


    @Autowired
    private DictCategoryBusinessService dictCategoryBusinessService;

    /**
     * 添加字典分类
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestVo){
        return dictCategoryBusinessService.save(requestVo);
    }




    /**
     * 编辑字典分类
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        return dictCategoryBusinessService.update(requestVo);
    }



    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO listPage(@RequestBody RequestJsonVO requestVo){
        return dictCategoryBusinessService.listPage(requestVo);
    }

    /**
     * 查询列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryList(@RequestBody RequestJsonVO requestVo){
        return dictCategoryBusinessService.queryList(requestVo);
    }


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        return dictCategoryBusinessService.findById(requestVo);
    }







    /**
     * 删除指定字典分类(仅限中台使用)
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        return dictCategoryBusinessService.deleteById(requestVo);
    }


    /**
     * 批量删除字典分类(仅限中台使用)
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        return dictCategoryBusinessService.deleteByIds(requestVo);
    }


}
