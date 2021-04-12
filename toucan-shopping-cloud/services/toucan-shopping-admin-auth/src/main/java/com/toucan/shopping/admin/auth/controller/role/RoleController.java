package com.toucan.shopping.admin.auth.controller.role;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.admin.auth.entity.AdminApp;
import com.toucan.shopping.admin.auth.entity.AdminRole;
import com.toucan.shopping.admin.auth.entity.App;
import com.toucan.shopping.admin.auth.entity.Role;
import com.toucan.shopping.admin.auth.page.AppPageInfo;
import com.toucan.shopping.admin.auth.page.RolePageInfo;
import com.toucan.shopping.admin.auth.service.AdminAppService;
import com.toucan.shopping.admin.auth.service.AdminRoleService;
import com.toucan.shopping.admin.auth.service.RoleService;
import com.toucan.shopping.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/role")
public class RoleController {


    private final Logger logger = LoggerFactory.getLogger(getClass());



    @Autowired
    private RoleService roleService;

    @Autowired
    private AdminRoleService adminRoleService;




    /**
     * 添加角色
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("添加失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            Role role = JSONObject.parseObject(requestVo.getEntityJson(),Role.class);
            if(StringUtils.isEmpty(role.getName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("添加失败,请输入角色名称");
                return resultObjectVO;
            }


            role.setRoleId(GlobalUUID.uuid());
            role.setCreateDate(new Date());
            role.setEnableStatus((short)1);
            role.setDeleteStatus((short)0);
            int row = roleService.save(role);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("添加失败,请重试!");
                return resultObjectVO;
            }

            resultObjectVO.setData(role);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("添加失败,请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 編輯角色
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            Role entity = JSONObject.parseObject(requestVo.getEntityJson(),Role.class);
            if(StringUtils.isEmpty(entity.getName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请传入角色名称");
                return resultObjectVO;
            }
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请传入角色ID");
                return resultObjectVO;
            }


            Role query=new Role();
            query.setId(entity.getId());
            query.setDeleteStatus((short)0);
            List<Role> roleList = roleService.findListByEntity(query);
            if(CollectionUtils.isEmpty(roleList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("该角色不存在!");
                return resultObjectVO;
            }

            entity.setUpdateDate(new Date());
            int row = roleService.update(entity);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请重试!");
                return resultObjectVO;
            }


            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO listPage(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            RolePageInfo rolePageInfo = JSONObject.parseObject(requestVo.getEntityJson(), RolePageInfo.class);
            resultObjectVO.setData(roleService.queryListPage(rolePageInfo));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            Role entity = JSONObject.parseObject(requestVo.getEntityJson(),Role.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到角色ID");
                return resultObjectVO;
            }

            //查询是否存在该角色
            Role query=new Role();
            query.setId(entity.getId());
            List<Role> appList = roleService.findListByEntity(query);
            if(CollectionUtils.isEmpty(appList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,角色不存在!");
                return resultObjectVO;
            }
            resultObjectVO.setData(appList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 根据ID查询所有角色
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/admin/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findListByAdminId(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            AdminVO adminVO = JSONObject.parseObject(requestVo.getEntityJson(), AdminVO.class);
            if(StringUtils.isEmpty(adminVO.getAdminId()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到AdminID");
                return resultObjectVO;
            }

            List<AdminRole> adminRoles = adminRoleService.findListByAdminId(adminVO.getAdminId());

            if(!CollectionUtils.isEmpty(adminRoles)) {
                List<Role> roles = new ArrayList<>();
                for(AdminRole adminRole:adminRoles)
                {
                    //查询是否存在该角色
                    Role query = new Role();
                    query.setRoleId(adminRole.getRoleId());
                    List<Role> roleList = roleService.findListByEntity(query);
                    if(!CollectionUtils.isEmpty(roleList))
                    {
                        roles.addAll(roleList);
                    }
                }
                resultObjectVO.setData(roles);
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
     * 删除指定角色
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            Role entity = JSONObject.parseObject(requestVo.getEntityJson(),Role.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到角色ID");
                return resultObjectVO;
            }

            //查询是否存在该角色
            Role query=new Role();
            query.setId(entity.getId());
            List<Role> roleList = roleService.findListByEntity(query);
            if(CollectionUtils.isEmpty(roleList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,角色不存在!");
                return resultObjectVO;
            }


            int row = roleService.deleteById(entity.getId());
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请重试!");
                return resultObjectVO;
            }

            //删除角色下所有关联
            AdminRole queryAdminRole =new AdminRole();
            queryAdminRole.setRoleId(roleList.get(0).getRoleId());

            List<AdminRole> adminRoles = adminRoleService.findListByEntity(queryAdminRole);
            if(!CollectionUtils.isEmpty(adminRoles)) {
                row = adminRoleService.deleteByRoleId(roleList.get(0).getId());

                if (row <= 0) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("请求失败,删除角色下所有管理账户失败!");
                    return resultObjectVO;
                }
            }

            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 批量删除角色
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<Role> roleList = JSONObject.parseArray(requestVo.getEntityJson(),Role.class);
            if(CollectionUtils.isEmpty(roleList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到角色ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(Role role:roleList) {
                if(role.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(role);

                    //查询是否存在该角色
                    Role query = new Role();
                    query.setId(role.getId());
                    List<Role> roleEntityList = roleService.findListByEntity(query);
                    if (CollectionUtils.isEmpty(roleEntityList)) {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        appResultObjectVO.setCode(ResultVO.FAILD);
                        appResultObjectVO.setMsg("请求失败,角色不存在!");
                        continue;
                    }


                    int row = roleService.deleteById(role.getId());
                    if (row < 1) {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        appResultObjectVO.setCode(ResultVO.FAILD);
                        appResultObjectVO.setMsg("请求失败,请重试!");
                        continue;
                    }

                    //删除角色下所有关联
                    AdminRole queryAdminRole =new AdminRole();
                    queryAdminRole.setRoleId(roleEntityList.get(0).getRoleId());

                    List<AdminRole> adminApps = adminRoleService.findListByEntity(queryAdminRole);
                    if(!CollectionUtils.isEmpty(adminApps)) {
                        row = adminRoleService.deleteByRoleId(roleEntityList.get(0).getId());

                        if (row <= 0) {
                            resultObjectVO.setCode(ResultVO.FAILD);
                            resultObjectVO.setMsg("请求失败,删除角色下所有关联账户失败!");
                            return resultObjectVO;
                        }
                    }

                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


}
