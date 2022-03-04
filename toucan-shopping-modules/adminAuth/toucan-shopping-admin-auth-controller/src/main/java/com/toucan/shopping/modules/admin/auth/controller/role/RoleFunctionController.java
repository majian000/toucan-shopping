package com.toucan.shopping.modules.admin.auth.controller.role;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.cache.service.RoleFunctionCacheService;
import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.entity.Role;
import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import com.toucan.shopping.modules.admin.auth.helper.AdminAuthCacheHelper;
import com.toucan.shopping.modules.admin.auth.page.RoleFunctionPageInfo;
import com.toucan.shopping.modules.admin.auth.page.RolePageInfo;
import com.toucan.shopping.modules.admin.auth.service.AdminRoleService;
import com.toucan.shopping.modules.admin.auth.service.FunctionService;
import com.toucan.shopping.modules.admin.auth.service.RoleFunctionService;
import com.toucan.shopping.modules.admin.auth.service.RoleService;
import com.toucan.shopping.modules.admin.auth.vo.*;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.GlobalUUID;
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
import java.util.LinkedList;
import java.util.List;

/**
 * 角色功能项管理
 */
@RestController
@RequestMapping("/roleFunction")
public class RoleFunctionController {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private RoleService roleService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private RoleFunctionService roleFunctionService;

    @Autowired
    private FunctionService functionService;




    /**
     * 查询指定角色的所有功能项
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/list",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryRoleFunctionList(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RoleFunction query = JSONObject.parseObject(requestJsonVO.getEntityJson(), RoleFunction.class);

            if(StringUtils.isEmpty(query.getRoleId()))
            {
                throw new IllegalArgumentException("roleId为空");
            }

            List<RoleFunction> roleFunctions = roleFunctionService.findListByEntity(query);
            if(!CollectionUtils.isEmpty(roleFunctions))
            {
                resultObjectVO.setData(roleFunctions);
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 保存角色功能项
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/save/functions",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO saveFunctions(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RoleFunctionVO entity = JSONObject.parseObject(requestJsonVO.getEntityJson(), RoleFunctionVO.class);
            if(StringUtils.isEmpty(entity.getRoleId()))
            {
                throw new IllegalArgumentException("roleId为空");
            }
            if(CollectionUtils.isEmpty(entity.getFunctions()))
            {
                throw new IllegalArgumentException("functions为空");
            }
            roleFunctionService.deleteByRoleId(entity.getRoleId());
            RoleFunction[] roleFunctions= new RoleFunction[entity.getFunctions().size()];
            int pos = 0;
            for(Function function:entity.getFunctions())
            {
                RoleFunction roleFunction = new RoleFunction();
                roleFunction.setRoleId(entity.getRoleId());
                roleFunction.setFunctionId(function.getFunctionId());
                roleFunction.setAppCode(entity.getAppCode());
                roleFunction.setCreateAdminId(entity.getCreateAdminId());
                roleFunction.setCreateDate(new Date());
                roleFunction.setDeleteStatus((short)0);

                roleFunctions[pos] = roleFunction;
                pos ++;
            }
            roleFunctionService.saves(roleFunctions);

            try{
                RoleFunctionCacheService roleFunctionCacheService = AdminAuthCacheHelper.getRoleFunctionCacheService();
                if(roleFunctionCacheService!=null) {
                    //先清空所有缓存,让权限校验的时候第一次从缓存中没有找到之后初始化,在某种意义上降低数据不一致性的风险
                    roleFunctionCacheService.deleteIndex();
                    //刷新到es缓存
                    if (roleFunctions != null && roleFunctions.length > 0) {
                        RoleFunctionCacheVO[] roleFunctionCacheVOS = new RoleFunctionCacheVO[roleFunctions.length];
                        for (int i = 0; i < roleFunctions.length; i++) {
                            RoleFunction roleFunction = roleFunctions[i];
                            RoleFunctionCacheVO roleFunctionCacheVO = new RoleFunctionCacheVO();
                            if (roleFunction != null) {
                                BeanUtils.copyProperties(roleFunctionCacheVO, roleFunction);
                            }
                            roleFunctionCacheVOS[i] = roleFunctionCacheVO;
                        }
                        roleFunctionCacheService.saves(roleFunctionCacheVOS);
                    }
                }

            }catch(Exception e)
            {
                resultObjectVO.setCode(ResultVO.SUCCESS);
                resultObjectVO.setMsg("更新缓存出现异常");
                logger.warn(e.getMessage(),e);
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO list(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            RoleFunctionPageInfo queryPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), RoleFunctionPageInfo.class);


            //查询角色 功能项关联
            PageInfo<RoleFunction> pageInfo =  roleFunctionService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    @RequestMapping(value="/query/function/tree/by/roleId/parentId",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryFunctionTreeByRoleIdAndParentId(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RoleFunctionVO query = requestVo.formatEntity(RoleFunctionVO.class);

            if(StringUtils.isEmpty(query.getRoleId()))
            {
                throw new IllegalArgumentException("roleId为空");
            }

            if(query.getPid()==null)
            {
                throw new IllegalArgumentException("pid为空");
            }

            //当前角色的所有关联项
            List<RoleFunction> roleFunctions = roleFunctionService.findListByEntity(query);
            //当前节点的子节点
            List<FunctionVO> functionVOS = functionService.queryOneLevelChildrenByIdAndAppCode(query.getPid(),query.getAppCode());

            List<FunctionTreeVO> functionTreeVOS = new LinkedList<>();
            for(FunctionVO functionVO:functionVOS)
            {
                FunctionTreeVO functionTreeVO = new FunctionTreeVO();
                BeanUtils.copyProperties(functionTreeVO,functionVO);
                functionTreeVOS.add(functionTreeVO);
            }

            //设置当前节点的半选状态
            for(FunctionTreeVO functionTreeVO:functionTreeVOS)
            {
                roleFunctionService.setHalfCheckAndIsParent(functionTreeVO,roleFunctions);
            }

            resultObjectVO.setData(functionTreeVOS);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




}
