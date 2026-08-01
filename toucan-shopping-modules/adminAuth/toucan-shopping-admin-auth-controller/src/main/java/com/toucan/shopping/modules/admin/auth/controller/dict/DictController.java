package com.toucan.shopping.modules.admin.auth.controller.dict;


import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.admin.auth.business.service.DictBusinessService;
import com.toucan.shopping.modules.admin.auth.entity.Dict;
import com.toucan.shopping.modules.admin.auth.page.DictPageInfo;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.service.AppService;
import com.toucan.shopping.modules.admin.auth.service.DictService;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.admin.auth.vo.DictTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 字典
 */
@RestController
@RequestMapping("/dict")
public class DictController {



    @Autowired
    private DictBusinessService dictBusinessService;


    /**
     * 添加字典
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestVo){
        return dictBusinessService.save(requestVo);
    }




    /**
     * 編輯字典
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        return dictBusinessService.update(requestVo);
    }



    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO listPage(@RequestBody RequestJsonVO requestVo){
        return dictBusinessService.listPage(requestVo);
    }


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        return dictBusinessService.findById(requestVo);
    }







    /**
     * 删除指定字典(仅限中台使用)
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        return dictBusinessService.deleteById(requestVo);
    }


    /**
     * 批量删除字典(仅限中台使用)
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        return dictBusinessService.deleteByIds(requestVo);
    }




    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table/by/pid", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeTableByPid(@RequestBody RequestJsonVO requestJsonVO){
        return dictBusinessService.queryTreeTableByPid(requestJsonVO);
    }



    /**
     * 查询指定节点下子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/child", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeChildByPid(@RequestBody RequestJsonVO requestJsonVO){
        return dictBusinessService.queryTreeChildByPid(requestJsonVO);
    }


    /**
     * 查询分类下的字典
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="query/dict/by/code/category/code", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryDictByCodeAndCategoryCode(@RequestBody RequestJsonVO requestJsonVO){
        return dictBusinessService.queryDictByCodeAndCategoryCode(requestJsonVO);
    }



    /**
     * 查询分类下的字典
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="query/dict/by/codes/category/code", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultTypeObjectVO<List<DictVO>> queryDictByCodesAndCategoryCode(@RequestBody RequestJsonVO requestJsonVO){
        return dictBusinessService.queryDictByCodesAndCategoryCode(requestJsonVO);
    }

}
