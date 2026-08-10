package com.toucan.shopping.modules.admin.auth.business.service;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.toucan.shopping.modules.admin.auth.entity.*;
import com.toucan.shopping.modules.admin.auth.page.AppPageInfo;
import com.toucan.shopping.modules.admin.auth.page.RoleFunctionPageInfo;
import com.toucan.shopping.modules.admin.auth.page.RolePageInfo;
import com.toucan.shopping.modules.admin.auth.service.*;
import com.toucan.shopping.modules.admin.auth.vo.*;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 */
@Service
public class RoleBusinessService {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private RoleService roleService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private RoleFunctionService roleFunctionService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminAppService adminAppService;

    @Autowired
    private AppService appService;

    @Autowired
    private FunctionService functionService;


    /**
     * 添加角色
     *
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Role role = JSONObject.parseObject(requestVo.getEntityJson(), Role.class);
            Check.notEmpty(role.getName(), ResultVO.FAILD, "添加失败,请输入角色名称");
            Check.isTrue(role.getName().length() <= 20, ResultVO.FAILD, "添加失败,角色名称不能超过20位");

            role.setRoleId(GlobalUUID.uuid());
            role.setDeleteStatus((short) 0);
            int row = roleService.save(role);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("添加失败,请重试!");
                return resultObjectVO;
            }

            resultObjectVO.setData(role);

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("添加失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 編輯角色
     *
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Role entity = JSONObject.parseObject(requestVo.getEntityJson(), Role.class);
            Check.notEmpty(entity.getName(), ResultVO.FAILD, "请传入角色名称");
            Check.isTrue(entity.getName().length() <= 20, ResultVO.FAILD, "角色名称不能超过20位");
            Check.notNull(entity.getId(), ResultVO.FAILD, "请传入角色ID");


            Role query = new Role();
            query.setId(entity.getId());
            query.setDeleteStatus((short) 0);
            List<Role> roleList = roleService.findListByEntity(query);
            if (CollectionUtils.isEmpty(roleList)) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("该角色不存在!");
                return resultObjectVO;
            }

            entity.setUpdateDate(new Date());
            int row = roleService.update(entity);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }


            resultObjectVO.setData(entity);

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询所有应用的角色树
     *
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryAdminRoleTree(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AdminAppVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), AdminAppVO.class);
            //查询查询指定用户下的应用角色树
            List<AdminApp> adminApps = adminAppService.findListByEntity(query);
            // 应用来源时只查询当前应用的数据
            if (query.getOperateSourceType() != null && query.getOperateSourceType().intValue() == 2
                && StringUtils.isNotEmpty(query.getOperateAppCode()) && !CollectionUtils.isEmpty(adminApps)) {
                adminApps.removeIf(a -> !query.getOperateAppCode().equals(a.getAppCode()));
            }
            if (!CollectionUtils.isEmpty(adminApps)) {
                List<App> apps = new ArrayList<App>();
                for (AdminApp adminApp : adminApps) {
                    App queryApp = new App();
                    queryApp.setCode(adminApp.getAppCode());
                    apps.addAll(appService.findListByEntity(queryApp));
                }
                List<RoleTreeVO> roleTreeVOS = new ArrayList<RoleTreeVO>();
                for (App app : apps) {
                    //查询所有应用
                    if (!CollectionUtils.isEmpty(apps)) {
                        RoleTreeVO roleTreeVO = new RoleTreeVO();
                        roleTreeVO.setId(app.getId());
                        roleTreeVO.setTitle(app.getCode() + " " + app.getName());
                        roleTreeVO.setText(app.getCode() + " " + app.getName());
                        roleTreeVO.setChildren(new ArrayList<RoleTreeVO>());
                        roleTreeVO.setRoleId("-1");

                        Role queryRole = new Role();
                        queryRole.setAppCode(app.getCode());
                        queryRole.setEnableStatus((short) 1);
                        queryRole.setDeleteStatus((short) 0);
                        //查询所有角色
                        List<Role> roles = roleService.findListByEntity(queryRole);
                        if (!CollectionUtils.isEmpty(roles)) {
                            for (Role role : roles) {
                                RoleTreeVO roleTreeChild = new RoleTreeVO();
                                roleTreeChild.setId(role.getId());
                                roleTreeChild.setTitle(role.getName());
                                roleTreeChild.setText(role.getName());
                                roleTreeChild.setRoleId(role.getRoleId());
                                roleTreeChild.setAppCode(role.getAppCode());

                                roleTreeVO.getChildren().add(roleTreeChild);
                            }

                        }

                        roleTreeVOS.add(roleTreeVO);
                    }
                }

                resultObjectVO.setData(roleTreeVOS);
            }

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询列表分页
     *
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO listPage(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            RolePageInfo rolePageInfo = JSONObject.parseObject(requestVo.getEntityJson(), RolePageInfo.class);
            resultObjectVO.setData(roleService.queryListPage(rolePageInfo));

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 根据ID查询
     *
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Role entity = JSONObject.parseObject(requestVo.getEntityJson(), Role.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到角色ID");

            //查询是否存在该角色
            Role query = new Role();
            query.setId(entity.getId());
            List<Role> roleList = roleService.findListByEntity(query);
            if (CollectionUtils.isEmpty(roleList)) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("角色不存在!");
                return resultObjectVO;
            }
            resultObjectVO.setData(roleList);

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 根据ID查询所有角色
     *
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findListByAdminId(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AdminVO adminVO = JSONObject.parseObject(requestVo.getEntityJson(), AdminVO.class);
            Check.notEmpty(adminVO.getAdminId(), ResultVO.FAILD, "没有找到AdminID");

            List<AdminRole> adminRoles = adminRoleService.findListByAdminId(adminVO.getAdminId());

            if (!CollectionUtils.isEmpty(adminRoles)) {
                List<Role> roles = new ArrayList<>();
                for (AdminRole adminRole : adminRoles) {
                    //查询是否存在该角色
                    Role query = new Role();
                    query.setRoleId(adminRole.getRoleId());
                    List<Role> roleList = roleService.findListByEntity(query);
                    if (!CollectionUtils.isEmpty(roleList)) {
                        roles.addAll(roleList);
                    }
                }
                resultObjectVO.setData(roles);
            }

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 删除指定角色
     *
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Role entity = JSONObject.parseObject(requestVo.getEntityJson(), Role.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到角色ID");

            //查询是否存在该角色
            Role query = new Role();
            query.setId(entity.getId());
            List<Role> roleList = roleService.findListByEntity(query);
            if (CollectionUtils.isEmpty(roleList)) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("角色不存在!");
                return resultObjectVO;
            }


            int row = roleService.deleteById(entity.getId());
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }


            //删除账号关联
            adminRoleService.deleteByRoleId(roleList.get(0).getRoleId());

            //删除功能关联
            roleFunctionService.deleteByRoleId(roleList.get(0).getRoleId());


            resultObjectVO.setData(entity);

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 批量删除角色
     *
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            List<Role> roleList = JSON.parseArray(requestVo.getEntityJson(), Role.class);
            Check.notEmpty(roleList, ResultVO.FAILD, "没有找到角色ID");
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for (Role role : roleList) {
                if (role.getId() != null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(role);

                    //查询是否存在该角色
                    Role query = new Role();
                    query.setId(role.getId());
                    List<Role> roleEntityList = roleService.findListByEntity(query);
                    if (CollectionUtils.isEmpty(roleEntityList)) {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("角色不存在!");
                        continue;
                    }


                    int row = roleService.deleteById(role.getId());
                    if (row < 1) {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请重试!");
                        continue;
                    }

                    //删除账号关联
                    adminRoleService.deleteByRoleId(roleList.get(0).getRoleId());

                    //删除功能关联
                    roleFunctionService.deleteByRoleId(roleList.get(0).getRoleId());

                }
            }
            resultObjectVO.setData(resultObjectVOList);

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryDetail(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RoleVO roleQuery = JSONObject.parseObject(requestVo.getEntityJson(), RoleVO.class);
            RoleDetailVO detail = new RoleDetailVO();
            RoleVO vo = roleService.findVOById(roleQuery.getId());
            if (vo != null) {
                // 应用来源时只返回当前应用的数据
                if (roleQuery.getOperateSourceType() != null && roleQuery.getOperateSourceType().intValue() == 2
                    && StringUtils.isNotEmpty(roleQuery.getOperateAppCode())) {
                    if (!roleQuery.getOperateAppCode().equals(vo.getAppCode())) {
                        vo = null;
                    }
                }
            }
            if (vo != null) {
                detail.setBasicInfo(vo);
            }
            resultObjectVO.setData(detail);
        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
        }
        return resultObjectVO;
    }


    /**
     * 查询角色功能列表(关联t_sa_function)
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public ResultObjectVO queryRoleFunctionListPage(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RoleFunctionPageInfo pageInfo = JSONObject.parseObject(requestVo.getEntityJson(), RoleFunctionPageInfo.class);
            PageInfo<RoleFunctionListVO> pageResult = roleFunctionService.queryRoleFunctionListPage(pageInfo);

            List<RoleFunctionListVO> list = pageResult.getList();
            if (!CollectionUtils.isEmpty(list)) {
                // 从角色获取appCode，只查询该应用下的功能项
                String roleAppCode = "-1";
                String roleId = pageInfo.getRoleId();
                if (StringUtils.isNotEmpty(roleId)) {
                    Role role = roleService.findByRoleId(roleId);
                    if (role != null) {
                        roleAppCode = role.getAppCode();
                    }
                }

                if (StringUtils.isNotEmpty(roleAppCode)) {
                    List<FunctionVO> allFunctions = functionService.findAllIdNamePid(roleAppCode);
                    Map<Long, FunctionVO> funcMap = new HashMap<>();
                    for (FunctionVO f : allFunctions) {
                        funcMap.put(f.getId(), f);
                    }

                    // 在内存中为每个VO构建层级路径 a 》 b 》 c
                    for (RoleFunctionListVO vo : list) {
                        StringBuilder path = new StringBuilder();
                        buildFunctionPath(vo.getFunctionEntityId(), funcMap, path);
                        vo.setFunctionUrl(path.toString());
                    }
                }
            }

            resultObjectVO.setData(pageResult);
        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
        }
        return resultObjectVO;
    }


    private void buildFunctionPath(Long funcId, Map<Long, FunctionVO> funcMap, StringBuilder path) {
        FunctionVO f = funcMap.get(funcId);
        if (f == null) {
            return;
        }
        if (f.getPid() != null && f.getPid() > 0) {
            buildFunctionPath(f.getPid(), funcMap, path);
        }
        if (path.length() > 0) {
            path.append(" 》 ");
        }
        path.append(f.getName());
    }


}
