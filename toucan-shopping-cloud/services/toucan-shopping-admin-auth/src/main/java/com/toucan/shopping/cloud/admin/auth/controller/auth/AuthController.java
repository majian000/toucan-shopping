package com.toucan.shopping.cloud.admin.auth.controller.auth;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.service.AdminRoleService;
import com.toucan.shopping.modules.admin.auth.service.AdminService;
import com.toucan.shopping.modules.admin.auth.service.RoleFunctionService;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminResultVO;
import com.toucan.shopping.modules.admin.auth.vo.AuthVerifyVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 校验权限
 */
@RestController
@RequestMapping("/auth")
public class AuthController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminAppService adminAppService;

    @Autowired
    private RoleFunctionService roleFunctionService;

    @Autowired
    private AdminRoleService adminRoleService;



    /**
     * 校验权限
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/verify",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO verify(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setData(false);
        if(requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(AdminResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到参数");
            return resultObjectVO;
        }
        try {
            AuthVerifyVO query = JSONObject.parseObject(requestVo.getEntityJson(), AuthVerifyVO.class);
            if(StringUtils.isEmpty(query.getAdminId()))
            {
                throw new IllegalArgumentException("adminId为空");
            }
            if(StringUtils.isEmpty(query.getAppCode())){
                throw new IllegalArgumentException("appCode为空");
            }
            if(StringUtils.isEmpty(query.getUrl()))
            {
                throw new IllegalArgumentException("url为空");
            }
            AdminRole queryAdminRole = new AdminRole();
            queryAdminRole.setAdminId(query.getAdminId());
            queryAdminRole.setAppCode(query.getAppCode());
            //查询这个账户下应用下的所有角色
            List<AdminRole> adminRoles = adminRoleService.findListByEntity(queryAdminRole);
            if(CollectionUtils.isNotEmpty(adminRoles)) {
                String[] roleIdArray = new String[adminRoles.size()];
                for(int i=0;i<adminRoles.size();i++) {
                    if(adminRoles.get(i)!=null) {
                        roleIdArray[i] = adminRoles.get(i).getRoleId();
                    }
                }
                Long count = roleFunctionService.findCountByAdminIdAndFunctionUrlAndAppCodeAndRoleIds(query.getUrl(),query.getAppCode(),roleIdArray);
                if(count!=null&&count.longValue()>0)
                {
                    resultObjectVO.setData(true);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



}
