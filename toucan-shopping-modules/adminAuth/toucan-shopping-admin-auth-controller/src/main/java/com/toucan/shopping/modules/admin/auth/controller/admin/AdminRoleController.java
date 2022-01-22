package com.toucan.shopping.modules.admin.auth.controller.admin;


import com.alibaba.fastjson.JSONObject;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 管理员应用 增删改查
 */
@RestController
@RequestMapping("/adminRole")
public class AdminRoleController {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private AdminAppService adminAppService;


    /**
     * 保存角色功能项
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/save/roles",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO saveRoles(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AdminRoleVO entity = JSONObject.parseObject(requestJsonVO.getEntityJson(), AdminRoleVO.class);
            if(StringUtils.isEmpty(entity.getAdminId()))
            {
                throw new IllegalArgumentException("adminId为空");
            }
            if(CollectionUtils.isEmpty(entity.getRoles()))
            {
                throw new IllegalArgumentException("roles为空");
            }

            AdminApp queryAdminApp = new AdminApp();
            queryAdminApp.setAdminId(entity.getCreateAdminId());

            List<AdminApp> adminApps = adminAppService.findListByEntity(queryAdminApp);

            if(CollectionUtils.isEmpty(adminApps))
            {
                throw new IllegalArgumentException("当前登录用户,关联应用列表为空");
            }

            //拿到当前这个账户的操作人可管理的所有应用
            String[] appCodes = new String[adminApps.size()];
            for(int i=0;i<adminApps.size();i++)
            {
                appCodes[i] = adminApps.get(i).getAppCode();
            }
            //创建账户角色的前提是这个账户和操作这个账户的操作人属于同一个应用,这个操作人也只能操作他俩所属同一应用下面的所有角色
            adminRoleService.deleteByAdminIdAndAppCodes(entity.getAdminId(),appCodes);

            int length=0;
            for(AdminRole adminRole:entity.getRoles())
            {
                //-1为应用节点
                if(!"-1".equals(adminRole.getRoleId())) {
                    adminRole.setAdminId(entity.getAdminId());
                    adminRole.setCreateAdminId(entity.getCreateAdminId());
                    adminRole.setCreateDate(new Date());
                    adminRole.setDeleteStatus((short) 0);
                    length++;
                }
            }
            AdminRole[] adminRoles = new AdminRole[length];
            int pos = 0;
            for(AdminRole adminRole:entity.getRoles()) {
                //-1为应用节点
                if (!"-1".equals(adminRole.getRoleId())) {
                    adminRoles[pos] = adminRole;
                    pos++;
                }
            }
            adminRoleService.saves(adminRoles);


            try {
                AdminRoleCacheService adminRoleCacheService = AdminAuthCacheHelper.getAdminRoleCacheService();
                if(adminRoleCacheService!=null) {
                    //同步缓存
                    List<String> deleteFaildIdList = new ArrayList<String>();
                    for (int i = 0; i < adminApps.size(); i++) {
                        //删除指定账号下的指定所有应用下的所有账号角色关联
                        adminRoleCacheService.deleteByAdminIdAndAppCodes(entity.getAdminId(), adminApps.get(i).getAppCode(), deleteFaildIdList);
                    }
                    if (adminRoles != null && adminRoles.length > 0) {
                        AdminRoleCacheVO[] adminRoleCacheVOS = new AdminRoleCacheVO[length];
                        for (int i = 0; i < adminRoles.length; i++) {
                            AdminRoleCacheVO adminRoleCacheVO = new AdminRoleCacheVO();
                            if (adminRoles[i] != null) {
                                BeanUtils.copyProperties(adminRoleCacheVO, adminRoles[i]);
                            }
                            adminRoleCacheVOS[i] = adminRoleCacheVO;
                        }
                        adminRoleCacheService.saves(adminRoleCacheVOS);
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
     * 根据实体查询对象
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryListByEntity",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListByEntity(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(AdminResultVO.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到参数");
            return resultObjectVO;
        }

        try {
            AdminRole queryAdminRole = JSONObject.parseObject(requestVo.getEntityJson(),AdminRole.class);
            List<AdminRole> adminRoles = adminRoleService.findListByEntity(queryAdminRole);
            resultObjectVO.setData(adminRoles);
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
            AdminRolePageInfo adminRolePageInfo = JSONObject.parseObject(requestVo.getEntityJson(), AdminRolePageInfo.class);


            //查询账号角色关联
            PageInfo<AdminRole> pageInfo =  adminRoleService.queryListPage(adminRolePageInfo);
            resultObjectVO.setData(pageInfo);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



}
