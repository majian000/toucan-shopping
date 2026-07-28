package com.toucan.shopping.modules.admin.auth.controller.admin;


import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.admin.auth.business.service.AdminRoleBusinessService;
import com.toucan.shopping.modules.admin.auth.cache.service.AdminRoleCacheService;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.helper.AdminAuthCacheHelper;
import com.toucan.shopping.modules.admin.auth.page.AdminRolePageInfo;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.service.AdminRoleService;
import com.toucan.shopping.modules.admin.auth.vo.AdminResultVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminRoleCacheVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminRoleVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 管理员应用 增删改查
 */
@RestController
@RequestMapping("/adminRole")
public class AdminRoleController {


    @Autowired
    private AdminRoleBusinessService adminRoleBusinessService;

    /**
     * 保存角色功能项
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/save/roles",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO saveRoles(@RequestBody RequestJsonVO requestJsonVO)
    {
        return adminRoleBusinessService.saveRoles(requestJsonVO);
    }





    /**
     * 根据实体查询对象
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryListByEntity", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListByEntity(@RequestBody RequestJsonVO requestVo){
        return adminRoleBusinessService.queryListByEntity(requestVo);
    }





    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO list(@RequestBody RequestJsonVO requestVo){
        return adminRoleBusinessService.list(requestVo);
    }



}
