package com.toucan.shopping.modules.admin.auth.controller.admin;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.business.service.AdminBusinessService;
import com.toucan.shopping.modules.admin.auth.cache.service.AdminRoleCacheService;
import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.helper.AdminAuthCacheHelper;
import com.toucan.shopping.modules.admin.auth.page.AdminPageInfo;
import com.toucan.shopping.modules.admin.auth.redis.AdminAuthRedisKey;
import com.toucan.shopping.modules.admin.auth.service.*;
import com.toucan.shopping.modules.admin.auth.service.impl.AdminLoginHistoryAsyncService;
import com.toucan.shopping.modules.admin.auth.service.impl.AdminLoginHistoryServiceImpl;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.AdminRegistUtil;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.util.MD5Util;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 管理员 增删改查
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminBusinessService adminBusinessService;

    /**
     * 保存管理员账户
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestVo){
        return adminBusinessService.save(requestVo);
    }




    /**
     * 根据实体查询对象
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryListByEntity",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryListByEntity(@RequestBody RequestJsonVO requestVo){
        return adminBusinessService.queryListByEntity(requestVo);
    }





    /**
     * 修改密码
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update/password",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updatePassword(@RequestBody RequestJsonVO requestVo){
        return adminBusinessService.updatePassword(requestVo);
    }



    /**
     * 管理员账户登录
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/login",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO login(@RequestBody RequestJsonVO requestVo){
        return adminBusinessService.login(requestVo);
    }


    /**
     * 管理员账户注销
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/logout",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO logout(@RequestBody RequestJsonVO requestVo){
        return adminBusinessService.logout(requestVo);
    }

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        return adminBusinessService.findById(requestVo);
    }


    /**
     * 查询管理员登录token
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/query/login/token",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryLoginToken(@RequestBody RequestJsonVO requestVo) {
        return adminBusinessService.queryLoginToken(requestVo);
    }





    /**
     * 判断是否在线
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/is/online",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO isOnline(@RequestBody RequestJsonVO requestVo) {
        return adminBusinessService.isOnline(requestVo);
    }


    /**
     * 編輯账号
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        return adminBusinessService.update(requestVo);
    }





    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO list(@RequestBody RequestJsonVO requestVo){
        return adminBusinessService.list(requestVo);
    }






    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        return adminBusinessService.deleteById(requestVo);
    }


    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        return adminBusinessService.deleteByIds(requestVo);
    }




}
