package com.toucan.shopping.modules.admin.auth.controller.app;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.business.service.AppBusinessService;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.helper.AdminAuthCacheHelper;
import com.toucan.shopping.modules.admin.auth.page.AppPageInfo;
import com.toucan.shopping.modules.admin.auth.redis.AdminAuthRedisKey;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.service.AppService;
import com.toucan.shopping.modules.admin.auth.service.OrgnazitionAppService;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.common.util.AlphabetNumberUtils;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 管理员应用管理
 */
@RestController
@RequestMapping("/app")
public class AppController {


    private final Logger logger = LoggerFactory.getLogger(getClass());



    @Autowired
    private AppService appService;

    @Autowired
    private AdminAppService adminAppService;

    @Autowired
    private OrgnazitionAppService orgnazitionAppService;

    @Autowired
    private AppBusinessService appBusinessService;


    /**
     * 添加应用
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestVo){
        return appBusinessService.save(requestVo);
    }




    /**
     * 編輯应用
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        return appBusinessService.update(requestVo);
    }



    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO listPage(@RequestBody RequestJsonVO requestVo){
        return appBusinessService.listPage(requestVo);
    }



    /**
     * 查询列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO list(@RequestBody RequestJsonVO requestVo){
        return appBusinessService.list(requestVo);
    }


    /**
     * 查询列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryListByCodes",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListByCodes(@RequestBody RequestJsonVO requestVo){
        return appBusinessService.queryListByCodes(requestVo);
    }


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        return appBusinessService.findById(requestVo);
    }


    /**
     * 根据编码查询启用状态
     * @param requestVo
     * @return true:启用 false:停用
     */
    @RequestMapping(value="/enable/status/by/code",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryEnableStatusByCode(@RequestBody RequestJsonVO requestVo){
        return appBusinessService.queryEnableStatusByCode(requestVo);
    }


    /**
     * 根据编码查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/code",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findByCode(@RequestBody RequestJsonVO requestVo){
        return appBusinessService.findByCode(requestVo);
    }


    /**
     * 删除指定应用
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        return appBusinessService.deleteById(requestVo);
    }


    /**
     * 批量删除应用
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        return appBusinessService.deleteByIds(requestVo);
    }


}
