package com.toucan.shopping.standard.admin.auth.proxy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import com.toucan.shopping.modules.admin.auth.service.AdminRoleService;
import com.toucan.shopping.modules.admin.auth.service.RoleFunctionService;
import com.toucan.shopping.modules.admin.auth.service.RoleService;
import com.toucan.shopping.modules.admin.auth.vo.RoleFunctionVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.standard.admin.auth.proxy.service.RoleFunctionServiceProxy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class RoleFunctionServiceProxyImpl implements RoleFunctionServiceProxy {



    private final Logger logger = LoggerFactory.getLogger(getClass());



    @Autowired
    private RoleService roleService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private RoleFunctionService roleFunctionService;






    /**
     * 查询指定角色的所有功能项
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO queryRoleFunctionList( RequestJsonVO requestJsonVO)
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
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 保存角色功能项
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO saveFunctions( RequestJsonVO requestJsonVO)
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

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }
}
