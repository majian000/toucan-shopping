package com.toucan.shopping.modules.admin.auth.controller.role;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.business.service.RoleBusinessService;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.entity.Role;
import com.toucan.shopping.modules.admin.auth.page.AppPageInfo;
import com.toucan.shopping.modules.admin.auth.page.RolePageInfo;
import com.toucan.shopping.modules.admin.auth.service.*;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.admin.auth.vo.RoleTreeVO;
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
 * 角色管理
 */
@RestController
@RequestMapping("/role")
public class RoleController {


    @Autowired
    private RoleBusinessService roleBusinessService;



    /**
     * 添加角色
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestVo){
        return roleBusinessService.save(requestVo);
    }




    /**
     * 編輯角色
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        return roleBusinessService.update(requestVo);
    }


    /**
     * 查询所有应用的角色树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/admin/role/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAdminRoleTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        return roleBusinessService.queryAdminRoleTree(requestJsonVO);
    }


    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO listPage(@RequestBody RequestJsonVO requestVo){
        return roleBusinessService.listPage(requestVo);
    }

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        return roleBusinessService.findById(requestVo);
    }


    /**
     * 根据ID查询所有角色
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/admin/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findListByAdminId(@RequestBody RequestJsonVO requestVo){
        return roleBusinessService.findListByAdminId(requestVo);
    }


    /**
     * 删除指定角色
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        return roleBusinessService.deleteById(requestVo);
    }


    /**
     * 批量删除角色
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        return roleBusinessService.deleteByIds(requestVo);
    }


}
