package com.toucan.shopping.cloud.admin.auth.controller.auth;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import com.toucan.shopping.modules.admin.auth.es.service.AdminRoleElasticSearchService;
import com.toucan.shopping.modules.admin.auth.redis.AdminCenterRedisKey;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.service.AdminRoleService;
import com.toucan.shopping.modules.admin.auth.service.AdminService;
import com.toucan.shopping.modules.admin.auth.service.RoleFunctionService;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminResultVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminRoleElasticSearchVO;
import com.toucan.shopping.modules.admin.auth.vo.AuthVerifyVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.concurrent.TimeUnit;

/**
 * 校验权限
 */
@RestController
@RequestMapping("/auth")
public class AuthController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RoleFunctionService roleFunctionService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AdminRoleElasticSearchService adminRoleElasticSearchService;

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

            List<AdminRole> adminRoles = null;
            //先查询缓存,为了缓解数据库的查询压力
            List<AdminRoleElasticSearchVO> adminRoleElasticSearchVOS = null;
            try {
                adminRoleElasticSearchVOS = adminRoleElasticSearchService.queryByEntity((AdminRoleElasticSearchVO) queryAdminRole, 1);
            }catch(Exception e)
            {
                logger.warn(e.getMessage(),e);
            }
            if(CollectionUtils.isNotEmpty(adminRoleElasticSearchVOS))
            {
                //将缓存数据进行格式化
                adminRoles = JSONObject.parseArray(JSONObject.toJSONString(adminRoleElasticSearchVOS),AdminRole.class);
            }else{
                //查询这个账户下应用下的所有角色
                adminRoles = adminRoleService.findListByEntity(queryAdminRole);
                try {
                    //同步缓存
                    if (CollectionUtils.isNotEmpty(adminRoles)) {
                        for (AdminRole adminRole : adminRoles) {
                            adminRoleElasticSearchService.save((AdminRoleElasticSearchVO) adminRole);
                        }
                    }
                }catch(Exception e)
                {
                    logger.warn(e.getMessage(),e);
                }
            }

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
                    //每次操作都延长登录会话1小时
                    redisTemplate.expire(AdminCenterRedisKey.getLoginTokenGroupKey(query.getAdminId()),
                            AdminCenterRedisKey.LOGIN_TIMEOUT_SECOND, TimeUnit.SECONDS);
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
