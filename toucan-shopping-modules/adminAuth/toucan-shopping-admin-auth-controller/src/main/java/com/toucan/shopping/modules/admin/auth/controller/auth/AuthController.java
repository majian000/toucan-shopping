package com.toucan.shopping.modules.admin.auth.controller.auth;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.business.service.AuthBusinessService;
import com.toucan.shopping.modules.admin.auth.cache.service.AdminRoleCacheService;
import com.toucan.shopping.modules.admin.auth.cache.service.FunctionCacheService;
import com.toucan.shopping.modules.admin.auth.cache.service.RoleFunctionCacheService;
import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import com.toucan.shopping.modules.admin.auth.helper.AdminAuthCacheHelper;
import com.toucan.shopping.modules.admin.auth.redis.AdminAuthRedisKey;
import com.toucan.shopping.modules.admin.auth.service.*;
import com.toucan.shopping.modules.admin.auth.vo.*;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 校验权限
 */
@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private AuthBusinessService authBusinessService;

    /**
     * 校验权限
     * 首先从es中查询权限关联,如果es中没有就查询数据库就进行一次同步,如果数据库也没有 就认为没有权限
     * 这样设计的好处是 让所有正常的用户请求全部走缓存,那些不正常的用户虽然最后也会查询到数据库层面,但是后续会做黑名单限制恶意用户的访问
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/verify",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO verify(@RequestBody RequestJsonVO requestVo){
        return authBusinessService.verify(requestVo);
    }








    /**
     * 校验权限
     * 首先从es中查询权限关联,如果es中没有就查询数据库就进行一次同步,如果数据库也没有 就认为没有权限
     * 这样设计的好处是 让所有正常的用户请求全部走缓存,那些不正常的用户虽然最后也会查询到数据库层面,但是后续会做黑名单限制恶意用户的访问
     * @param requestVo
     * @return -1 登录超时 -2 权限校验失败 1成功
     */
    @RequestMapping(value="/verifyLoginAndUrl",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO verifyLoginAndUrl(@RequestBody RequestJsonVO requestVo){
        return authBusinessService.verifyLoginAndUrl(requestVo);
    }



}
