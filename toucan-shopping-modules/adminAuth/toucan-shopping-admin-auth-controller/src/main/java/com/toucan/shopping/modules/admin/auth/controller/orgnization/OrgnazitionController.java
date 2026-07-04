package com.toucan.shopping.modules.admin.auth.controller.orgnization;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.business.service.OrgnazitionBusinessService;
import com.toucan.shopping.modules.admin.auth.entity.*;
import com.toucan.shopping.modules.admin.auth.page.OrgnazitionTreeInfo;
import com.toucan.shopping.modules.admin.auth.service.*;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.admin.auth.vo.OrgnazitionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.OrgnazitionVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.CodeUtils;
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
import java.util.List;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 组织机构管理
 */
@RestController
@RequestMapping("/orgnazition")
public class OrgnazitionController {


    @Autowired
    private OrgnazitionBusinessService orgnazitionBusinessService;


    /**
     * 添加组织机构
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestVo){
        return orgnazitionBusinessService.save(requestVo);
    }




    /**
     * 編輯组织机构
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        return orgnazitionBusinessService.update(requestVo);
    }



    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAppOrgnazitionTreeTable(@RequestBody RequestJsonVO requestJsonVO){
        return orgnazitionBusinessService.queryAppOrgnazitionTreeTable(requestJsonVO);
    }

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        return orgnazitionBusinessService.findById(requestVo);
    }








    /**
     * 查询当前账号下组织机构树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/admin/orgnazition/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAdminOrgnazitionTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        return orgnazitionBusinessService.queryAdminOrgnazitionTree(requestJsonVO);
    }





    /**
     * 删除指定组织机构(仅限中台使用)
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        return orgnazitionBusinessService.deleteById(requestVo);
    }


    /**
     * 批量删除组织机构(仅限中台使用)
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        return orgnazitionBusinessService.deleteByIds(requestVo);
    }


    /**
     * 查询组织机构树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/orgnazation/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryOrgnazationTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        return orgnazitionBusinessService.queryOrgnazationTree(requestJsonVO);
    }





}
