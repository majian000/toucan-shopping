package com.toucan.shopping.modules.admin.auth.business.service;


import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.toucan.shopping.modules.admin.auth.cache.service.AdminRoleCacheService;
import com.toucan.shopping.modules.admin.auth.cache.service.FunctionCacheService;
import com.toucan.shopping.modules.admin.auth.cache.service.RoleFunctionCacheService;
import com.toucan.shopping.modules.admin.auth.entity.*;
import com.toucan.shopping.modules.admin.auth.helper.AdminAuthCacheHelper;
import com.toucan.shopping.modules.admin.auth.page.FunctionTreeInfo;
import com.toucan.shopping.modules.admin.auth.service.*;
import com.toucan.shopping.modules.admin.auth.vo.*;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能项管理
 */
@Service
public class FunctionBusinessService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FunctionService functionService;

    @Autowired
    private AppService appService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private AdminAppService adminAppService;

    @Autowired
    private RoleFunctionService roleFunctionService;


    /**
     * 添加功能项
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Function entity = JSONObject.parseObject(requestVo.getEntityJson(),Function.class);
            Check.notEmpty(entity.getName(), ResultVO.FAILD, "添加失败,请输入功能项名称");


            entity.setFunctionId(GlobalUUID.uuid());
            entity.setCreateDate(new Date());
            entity.setDeleteStatus((short)0);
            int row = functionService.save(entity);
            if (row < 1) {
                return ResultObjectVO.fail(ResultVO.FAILD, "添加失败,请重试!");
            }

            resultObjectVO.setData(entity);

            try {
                FunctionCacheService functionCacheService = AdminAuthCacheHelper.getFunctionCacheService();
                if(functionCacheService!=null) {
                    //同步es缓存,在这里要求缓存和数据库是一致的
                    FunctionCacheVO functionCacheVO = new FunctionCacheVO();
                    BeanUtils.copyProperties(functionCacheVO, entity);
                    functionCacheService.save(functionCacheVO);
                }
            }catch(Exception e) {
                resultObjectVO.setCode(ResultVO.SUCCESS);
                resultObjectVO.setMsg("更新缓存出现异常");
                logger.warn(e.getMessage(), e);
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("添加失败,请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 添加功能项
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO saves(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            List<Function> entitys = requestVo.formatEntityList(Function.class);

            for(Function entity:entitys) {
                Check.notEmpty(entity.getName(), ResultVO.FAILD, "添加失败,请输入功能项名称");


                entity.setFunctionId(GlobalUUID.uuid());
                entity.setCreateDate(new Date());
                entity.setDeleteStatus((short) 0);
                int row = functionService.save(entity);
                if (row < 1) {
                    return ResultObjectVO.fail(ResultVO.FAILD, "添加失败,请重试!");
                }

                resultObjectVO.setData(entity);

                try {
                    FunctionCacheService functionCacheService = AdminAuthCacheHelper.getFunctionCacheService();
                    if (functionCacheService != null) {
                        //同步es缓存,在这里要求缓存和数据库是一致的
                        FunctionCacheVO functionCacheVO = new FunctionCacheVO();
                        BeanUtils.copyProperties(functionCacheVO, entity);
                        functionCacheService.save(functionCacheVO);
                    }
                } catch (Exception e) {
                    resultObjectVO.setCode(ResultVO.SUCCESS);
                    resultObjectVO.setMsg("更新缓存出现异常");
                    logger.warn(e.getMessage(), e);
                }
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("添加失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询应用权限列表
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryAppFunctionTree(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            App query = JSONObject.parseObject(requestJsonVO.getEntityJson(), App.class);

            //查询所有应用
            List<App> apps = appService.findListByEntity(query);
            if(!CollectionUtils.isEmpty(apps))
            {
                List<AppFunctionTreeVO> appFunctionTreeVOS = new ArrayList<AppFunctionTreeVO>();
                for(App app : apps)
                {
                    AppFunctionTreeVO appFunctionTreeVO = new AppFunctionTreeVO();
                    appFunctionTreeVO.setId(-1L);
                    appFunctionTreeVO.setAppCode(app.getCode());
                    appFunctionTreeVO.setTitle(app.getName());
                    appFunctionTreeVO.setEnableStatus((short)1);
                    appFunctionTreeVOS.add(appFunctionTreeVO);

                    appFunctionTreeVO.setChildren(functionService.queryTreeByAppCode(app.getCode()));

                }
                resultObjectVO.setData(appFunctionTreeVOS);
            }

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 查询应用权限列表
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryAppFunctionTreeByPid(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            FunctionTreeVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), FunctionTreeVO.class);
            List<FunctionVO> functionVOS = functionService.queryOneLevelChildrenByIdAndAppCode(query.getParentId(),query.getAppCode());
            List<FunctionTreeVO> functionTreeVOS = new LinkedList<>();
            for(FunctionVO functionVO:functionVOS)
            {
                FunctionTreeVO functionTreeVO = new FunctionTreeVO();
                BeanUtils.copyProperties(functionTreeVO,functionVO);
                Long childrenCount = functionService.queryOneLevelChildrenCountByIdAndAppCode(functionVO.getId(),functionVO.getAppCode());
                if(childrenCount!=null&&childrenCount.longValue()>0)
                {
                    functionTreeVO.setIsParent(true);
                }else{
                    functionTreeVO.setIsParent(false);
                }
                functionTreeVOS.add(functionTreeVO);
            }
            resultObjectVO.setData(functionTreeVOS);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询指定用户和应用的权限树
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryFunctionTree(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            App query = JSONObject.parseObject(requestJsonVO.getEntityJson(), App.class);
            //查询指定应用下的权限树
            resultObjectVO.setData(functionService.queryTreeByAppCode(query.getCode()));

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 編輯功能项
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Function entity = JSONObject.parseObject(requestVo.getEntityJson(),Function.class);

            if(entity.getId().longValue()==entity.getPid().longValue())
            {
                logger.info("上级节点不能为自己 param:"+ JSONObject.toJSONString(entity));
                return ResultObjectVO.fail(ResultVO.FAILD, "上级节点不能为自己!");
            }
            Check.notEmpty(entity.getName(), ResultVO.FAILD, "请传入功能项名称");
            Check.notNull(entity.getId(), ResultVO.FAILD, "请传入功能项ID");


            Function query=new Function();
            query.setId(entity.getId());
            query.setDeleteStatus((short)0);
            List<Function> functions = functionService.findListByEntity(query);
            if(CollectionUtils.isEmpty(functions))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "该功能项不存在!");
            }

            entity.setUpdateDate(new Date());
            int row = functionService.update(entity);
            if (row < 1) {
                return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
            }


            try{
                FunctionCacheService functionCacheService = AdminAuthCacheHelper.getFunctionCacheService();
                if(functionCacheService!=null) {
                    //同步es缓存
                    FunctionCacheVO functionCacheVO = new FunctionCacheVO();
                    BeanUtils.copyProperties(functionCacheVO, entity);
                    functionCacheService.update(functionCacheVO);
                }
            }catch(Exception e) {
                resultObjectVO.setCode(ResultVO.SUCCESS);
                resultObjectVO.setMsg("更新缓存出现异常");
                logger.warn(e.getMessage(), e);
            }

            //如果修改了上级功能项,删除旧的角色功能关联
            if(functions.get(0).getPid().longValue()!=entity.getPid().longValue())
            {
                //删除所有子节点与角色关联
                List<Function> children = new ArrayList<Function>();
                functionService.queryChildren(children,functions.get(0));
                if(!CollectionUtils.isEmpty(children))
                {
                    String[] functionIdArray= new String[children.size()];
                    for(int i=0;i<children.size();i++)
                    {
                        functionIdArray[i] = children.get(i).getFunctionId();
                    }
                    roleFunctionService.deleteByFunctionIdArray(functionIdArray);
                }

                //删除当前节点与角色关联
                roleFunctionService.deleteByFunctionId(functions.get(0).getFunctionId());


                try {
                    RoleFunctionCacheService roleFunctionCacheService = AdminAuthCacheHelper.getRoleFunctionCacheService();
                    if(roleFunctionCacheService!=null) {
                        List<String> deleteFaildIdList = new ArrayList<String>();
                        roleFunctionCacheService.deleteByFunctionId(functions.get(0).getFunctionId(), deleteFaildIdList);
                    }
                }catch(Exception e) {
                    resultObjectVO.setCode(ResultVO.SUCCESS);
                    resultObjectVO.setMsg("更新缓存出现异常");
                    logger.warn(e.getMessage(), e);
                }
            }

            resultObjectVO.setData(entity);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryAppFunctionTreeTable(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            FunctionTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), FunctionTreeInfo.class);
            if(StringUtils.isEmpty(queryPageInfo.getAdminId()))
            {
                throw new IllegalArgumentException("adminId为空");
            }

            //查询账号下关联应用
            AdminApp queryAdminApp = new AdminApp();
            queryAdminApp.setAdminId(queryPageInfo.getAdminId());
            queryAdminApp.setAppCode(queryPageInfo.getAppCode());

            //当前用户下关联所有应用
            List<AdminAppVO> adminApps = adminAppService.findAppListByAdminAppEntity(queryAdminApp);
            if(!CollectionUtils.isEmpty(adminApps))
            {

                List<Object> appFunctionTreeVOS = new ArrayList<Object>();
                for(AdminAppVO adminAppVO : adminApps)
                {
                    AppFunctionTreeVO appFunctionTreeVO = new AppFunctionTreeVO();
                    appFunctionTreeVO.setId(adminAppVO.getId());
                    appFunctionTreeVO.setAppCode(adminAppVO.getAppCode());
                    appFunctionTreeVO.setName(adminAppVO.getAppCode()+" "+adminAppVO.getName());
                    appFunctionTreeVO.setPid(-1L);
                    appFunctionTreeVO.setEnableStatus((short)1);
                    appFunctionTreeVOS.add(appFunctionTreeVO);

                    //查询查询这个APP下的功能项列表
                    queryPageInfo.setAppCode(adminAppVO.getAppCode());
                    List<Function>  functions = functionService.findTreeTable(queryPageInfo);
                    for(Function function:functions)
                    {
                        //将-1替换成adminApp的id
                        if(function.getPid().longValue()==-1)
                        {
                            function.setPid(appFunctionTreeVO.getId());
                        }
                    }
                    appFunctionTreeVOS.addAll(functions);

                }
                resultObjectVO.setData(appFunctionTreeVOS);
            }

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 根据PID查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryAppFunctionTreeTableByPid(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            FunctionTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), FunctionTreeInfo.class);
            if(StringUtils.isEmpty(queryPageInfo.getAdminId()))
            {
                throw new IllegalArgumentException("adminId为空");
            }

            //查询账号下关联应用
            AdminApp queryAdminApp = new AdminApp();
            queryAdminApp.setAdminId(queryPageInfo.getAdminId());
            queryAdminApp.setAppCode(queryPageInfo.getAppCode());


            List<AppFunctionTreeVO> functionTreeVOS = new ArrayList<AppFunctionTreeVO>();
            //条件查询
            if(StringUtils.isNotEmpty(queryPageInfo.getName())||
                    StringUtils.isNotEmpty(queryPageInfo.getUrl())||
                    StringUtils.isNotEmpty(queryPageInfo.getPermission())||
                    queryPageInfo.getEnableStatus()!=null||
                    StringUtils.isNotEmpty(queryPageInfo.getFunctionId()))
            {
                Function queryFunction = new Function();
                BeanUtils.copyProperties(queryFunction,queryPageInfo);
                List<FunctionVO> functions = functionService.findVOListByEntityFieldLike(queryFunction);
                for(FunctionVO function:functions)
                {
                    AppFunctionTreeVO appFunctionTreeVO = new AppFunctionTreeVO();
                    BeanUtils.copyProperties(appFunctionTreeVO,function);
                    functionTreeVOS.add(appFunctionTreeVO);
                }
                resultObjectVO.setData(functionTreeVOS);
            }else if(queryPageInfo.getPid()==null) { //页面初始化
                if(StringUtils.isEmpty(queryAdminApp.getAppCode()))
                {
                    //从权限中台列表进入,查询当前所有应用
                    App query=new App();
                    query.setEnableStatus((short)1);
                    List<App> apps = appService.findListByEntity(query);
                    if (!CollectionUtils.isEmpty(apps)) {
                        List<Object> appFunctionTreeVOS = new ArrayList<Object>();
                        //虚拟一个应用节点的ID
                        long rootNodeId = -1L;
                        for (App app : apps) {
                            AppFunctionTreeVO appFunctionTreeVO = new AppFunctionTreeVO();
                            appFunctionTreeVO.setId(rootNodeId);
                            appFunctionTreeVO.setAppCode(app.getCode());
                            appFunctionTreeVO.setName(app.getCode() + " " + app.getName());
                            appFunctionTreeVO.setPid(-1L);
                            appFunctionTreeVO.setEnableStatus((short) 1);

                            //设置是否有子节点
                            Function queryFunction = new Function();
                            queryFunction.setAppCode(app.getCode());
                            queryFunction.setPid(-1L);
                            List<Function> functions = functionService.findListByEntity(queryFunction);
                            if(!CollectionUtils.isEmpty(functions))
                            {
                                appFunctionTreeVO.setHaveChild(true);
                            }

                            appFunctionTreeVOS.add(appFunctionTreeVO);

                            rootNodeId -= 1;

                        }
                        resultObjectVO.setData(appFunctionTreeVOS);
                    }
                }else {
                    //当前用户下关联那个应用
                    App app = appService.findByAppCode(queryAdminApp.getAppCode());
                    if (app!=null) {
                        List<Object> appFunctionTreeVOS = new ArrayList<Object>();
                        //虚拟一个应用节点的ID
                        long rootNodeId = -1L;
                        AppFunctionTreeVO appFunctionTreeVO = new AppFunctionTreeVO();
                        appFunctionTreeVO.setId(rootNodeId);
                        appFunctionTreeVO.setAppCode(app.getCode());
                        appFunctionTreeVO.setName(app.getCode() + " " + app.getName());
                        appFunctionTreeVO.setPid(-1L);
                        appFunctionTreeVO.setEnableStatus((short) 1);

                        //设置是否有子节点
                        Function queryFunction = new Function();
                        queryFunction.setAppCode(app.getCode());
                        queryFunction.setPid(-1L);
                        List<Function> functions = functionService.findListByEntity(queryFunction);
                        if (!CollectionUtils.isEmpty(functions)) {
                            appFunctionTreeVO.setHaveChild(true);
                        }

                        appFunctionTreeVOS.add(appFunctionTreeVO);

                        resultObjectVO.setData(appFunctionTreeVOS);
                    }
                }
            }else{
                //查询查询这个APP下的功能项列表
                Function queryFunction = new Function();
                queryFunction.setAppCode(queryPageInfo.getAppCode());
                //如果点击应用节点的话,就查询下面的一级节点,否则就查询对应节点下的子节点
                if(queryPageInfo.getPid()<0)
                {
                    //设置查询顶级节点
                    queryFunction.setPid(-1L);
                }else{
                    queryFunction.setPid(queryPageInfo.getPid());
                }
                List<FunctionVO> functions = functionService.findVOListByEntity(queryFunction);
                for(FunctionVO function:functions)
                {
                    AppFunctionTreeVO appFunctionTreeVO = new AppFunctionTreeVO();
                    BeanUtils.copyProperties(appFunctionTreeVO,function);
                    queryFunction = new Function();
                    queryFunction.setPid(function.getId());
                    List<Function> functionChilds = functionService.findListByEntity(queryFunction);
                    //查询该节点是否存在子节点
                    if(!CollectionUtils.isEmpty(functionChilds))
                    {
                        appFunctionTreeVO.setHaveChild(true);
                    }
                    functionTreeVOS.add(appFunctionTreeVO);
                }
                resultObjectVO.setData(functionTreeVOS);
            }

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Function entity = JSONObject.parseObject(requestVo.getEntityJson(),Function.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到功能项ID");

            //查询是否存在该功能项
            Function query=new Function();
            query.setId(entity.getId());
            List<Function> listByEntity = functionService.findListByEntity(query);
            if(CollectionUtils.isEmpty(listByEntity))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "功能项不存在!");
            }
            resultObjectVO.setData(listByEntity);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 删除指定功能项
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Function entity = JSONObject.parseObject(requestVo.getEntityJson(),Function.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到功能项ID");

            List<Function> chidlren = new ArrayList<Function>();
            //查询当前节点下所有的子节点
            functionService.queryChildren(chidlren,entity);

            //把当前功能项添加进去,循环这个集合
            chidlren.add(entity);

            for(Function f:chidlren) {
                //删除当前功能项
                int row = functionService.deleteById(f.getId());
                if (row < 1) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("请重试!");
                    continue;
                }

                //删除角色功能项关联
                RoleFunction queryRoleFunction = new RoleFunction();
                queryRoleFunction.setFunctionId(f.getFunctionId());

                List<RoleFunction> roleFunction = roleFunctionService.findListByEntity(queryRoleFunction);
                if (!CollectionUtils.isEmpty(roleFunction)) {
                    row = roleFunctionService.deleteByFunctionId(f.getFunctionId());
                }


                try {
                    FunctionCacheService functionCacheService = AdminAuthCacheHelper.getFunctionCacheService();
                    RoleFunctionCacheService roleFunctionCacheService = AdminAuthCacheHelper.getRoleFunctionCacheService();
                    //刷新缓存
                    if(functionCacheService!=null) {
                        functionCacheService.deleteById(String.valueOf(f.getId()));
                        if (!CollectionUtils.isEmpty(roleFunction)) {
                            List<String> deleteFaildIdList = new ArrayList<String>();
                            if(roleFunctionCacheService!=null) {
                                roleFunctionCacheService.deleteByFunctionId(f.getFunctionId(), deleteFaildIdList);
                            }
                        }
                    }
                }catch(Exception e)
                {
                    resultObjectVO.setCode(ResultVO.SUCCESS);
                    resultObjectVO.setMsg("更新缓存出现异常");
                    logger.warn(e.getMessage(),e);
                }

            }

            resultObjectVO.setData(entity);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 清空该应用下所有功能项
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByAppCode(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Function entity = JSONObject.parseObject(requestVo.getEntityJson(),Function.class);
            Check.notNull(entity.getAppCode(), ResultVO.FAILD, "没有找到功能项ID");


            List<FunctionVO> chidlren =functionService.queryListByAppCode(entity.getAppCode());

            for(Function f:chidlren) {
                //删除当前功能项
                int row = functionService.deleteById(f.getId());
                if (row < 1) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("请重试!");
                    continue;
                }

                //删除功能项下所有角色关联
                roleFunctionService.deleteByFunctionId(f.getFunctionId());



                try {
                    FunctionCacheService functionCacheService = AdminAuthCacheHelper.getFunctionCacheService();
                    RoleFunctionCacheService roleFunctionCacheService = AdminAuthCacheHelper.getRoleFunctionCacheService();
                    //刷新缓存
                    if(functionCacheService!=null) {
                        functionCacheService.deleteById(String.valueOf(f.getId()));
                        List<String> deleteFaildIdList = new ArrayList<String>();
                        if(roleFunctionCacheService!=null) {
                            roleFunctionCacheService.deleteByFunctionId(f.getFunctionId(), deleteFaildIdList);
                        }
                    }
                }catch(Exception e)
                {
                    resultObjectVO.setCode(ResultVO.SUCCESS);
                    resultObjectVO.setMsg("更新缓存出现异常");
                    logger.warn(e.getMessage(),e);
                }

            }

            resultObjectVO.setData(entity);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 批量删除功能项
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            List<Function> functionList = JSON.parseArray(requestVo.getEntityJson(),Function.class);
            Check.notEmpty(functionList, ResultVO.FAILD, "没有找到功能项ID");
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(Function function:functionList) {
                if(function.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(function);


                    List<Function> chidlren = new ArrayList<Function>();
                    functionService.queryChildren(chidlren,function);

                    //把当前功能项添加进去,循环这个集合
                    chidlren.add(function);

                    for(Function f:chidlren) {
                        //删除当前功能项
                        int row = functionService.deleteById(f.getId());
                        if (row < 1) {
                            resultObjectVO.setCode(ResultVO.FAILD);
                            resultObjectVO.setMsg("请重试!");
                            continue;
                        }

                        //删除功能项下所有关联
                        RoleFunction queryRoleFunction = new RoleFunction();
                        queryRoleFunction.setFunctionId(f.getFunctionId());

                        List<RoleFunction> roleFunction = roleFunctionService.findListByEntity(queryRoleFunction);
                        if (!CollectionUtils.isEmpty(roleFunction)) {
                            roleFunctionService.deleteByFunctionId(f.getFunctionId());
                        }


                        try{
                            FunctionCacheService functionCacheService = AdminAuthCacheHelper.getFunctionCacheService();
                            RoleFunctionCacheService roleFunctionCacheService = AdminAuthCacheHelper.getRoleFunctionCacheService();
                            if(functionCacheService!=null) {
                                //刷新缓存
                                functionCacheService.deleteById(String.valueOf(f.getId()));
                                if (!CollectionUtils.isEmpty(roleFunction)) {
                                    List<String> deleteFaildIdList = new ArrayList<String>();
                                    if(roleFunctionCacheService!=null) {
                                        roleFunctionCacheService.deleteByFunctionId(f.getFunctionId(), deleteFaildIdList);
                                    }
                                }
                            }
                        }catch(Exception e)
                        {
                            resultObjectVO.setCode(ResultVO.SUCCESS);
                            resultObjectVO.setMsg("更新缓存出现异常");
                            logger.warn(e.getMessage(),e);
                        }
                    }

                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 查询指定管理员应用所有角色的功能项
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryAdminAppFunctions(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AdminApp query = JSONObject.parseObject(requestJsonVO.getEntityJson(), AdminApp.class);
            if(StringUtils.isEmpty(query.getAdminId()))
            {
                throw new IllegalArgumentException("adminId为空");
            }
            if(StringUtils.isEmpty(query.getAppCode()))
            {
                throw new IllegalArgumentException("appCode为空");
            }
            List<AdminRole> adminRoles = adminRoleService.listByAdminIdAndAppCode(query.getAdminId(),query.getAppCode());
            if(!CollectionUtils.isEmpty(adminRoles))
            {
                String[] roleIdArray = new String[adminRoles.size()];
                int pos =0 ;
                for(AdminRole adminRole:adminRoles)
                {
                    roleIdArray[pos]=adminRole.getRoleId();
                    pos++;
                }
                // 过滤掉已禁用的角色
                List<Role> roles = roleService.findListByRoleIds(roleIdArray);
                String[] enableRoleIdArray = roleIdArray;
                if(!CollectionUtils.isEmpty(roles))
                {
                    List<String> enableRoleIds = new ArrayList<>();
                    for(Role role : roles)
                    {
                        if(role.getEnableStatus() != null && role.getEnableStatus().shortValue() == 1)
                        {
                            enableRoleIds.add(role.getRoleId());
                        }
                    }
                    enableRoleIdArray = enableRoleIds.toArray(new String[0]);
                }
                resultObjectVO.setData(functionService.queryListByRoleIdArray(enableRoleIdArray));
            }


        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 返回一级子节点
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryChildren(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            Function query = JSONObject.parseObject(requestJsonVO.getEntityJson(), Function.class);
            //根据URL查询出功能项
            List<Function> childs = functionService.findListByEntity(query);
            if(!CollectionUtils.isEmpty(childs)) {
                query = new Function();
                query.setPid(childs.get(0).getId());
                query.setAppCode(childs.get(0).getAppCode());
                resultObjectVO.setData(functionService.findListByEntity(query));
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 返回指定人的指定应用的某个上级功能项下的按钮列表
     * 首先从es中查询权限关联,如果es中没有就查询数据库就进行一次同步,如果数据库也没有 就认为没有数据
     * 这样设计的好处是 让所有正常的用户请求全部走缓存,那些不正常的用户虽然最后也会查询到数据库层面,但是后续会做黑名单限制恶意用户的访问
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryOneChildsByAdminIdAndAppCodeAndParentUrl(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            //缓存是否可读取
            boolean cacheIsRead = true;
            FunctionVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), FunctionVO.class);
            AdminRoleCacheVO queryAdminRoleCacheVO = new AdminRoleCacheVO();
            queryAdminRoleCacheVO.setAdminId(query.getAdminId());
            queryAdminRoleCacheVO.setAppCode(query.getAppCode());
            List<AdminRole> adminRoles = null;
            List<AdminRoleCacheVO> adminRoleCacheVOS = null;
            AdminRoleCacheService adminRoleCacheService = AdminAuthCacheHelper.getAdminRoleCacheService();
            FunctionCacheService functionCacheService = AdminAuthCacheHelper.getFunctionCacheService();
            try {
                if(adminRoleCacheService!=null) {
                    adminRoleCacheVOS = adminRoleCacheService.queryByEntity(queryAdminRoleCacheVO);
                    if (!CollectionUtils.isEmpty(adminRoleCacheVOS)) {
                        adminRoles = JSON.parseArray(JSONObject.toJSONString(adminRoleCacheVOS), AdminRole.class);
                    }
                }
            }catch(Exception e)
            {
                cacheIsRead = false;
                logger.warn(e.getMessage(),e);
            }
            //将数据库数据同步到缓存
            if(CollectionUtils.isEmpty(adminRoles)) {
                adminRoles = adminRoleService.listByAdminIdAndAppCode(query.getAdminId(), query.getAppCode());
                try {
                    if (!CollectionUtils.isEmpty(adminRoles)&&cacheIsRead) {
                        if(adminRoleCacheService!=null) {
                            for (AdminRole adminRole : adminRoles) {
                                AdminRoleCacheVO adminRoleCacheVO = new AdminRoleCacheVO();
                                BeanUtils.copyProperties(adminRoleCacheVO, adminRole);
                                adminRoleCacheService.save(adminRoleCacheVO);
                            }
                        }
                    }
                }catch(Exception e)
                {
                    cacheIsRead = false;
                    logger.warn(e.getMessage(),e);
                }
            }
            if(!CollectionUtils.isEmpty(adminRoles))
            {
                List<Function> functions = null;
                List<FunctionCacheVO> functionCacheVOS = null;
                try {
                    if(cacheIsRead) {
                        if(functionCacheService!=null) {
                            //从缓存查询数据
                            FunctionCacheVO queryFunctionCacheVO = new FunctionCacheVO();
                            BeanUtils.copyProperties(queryFunctionCacheVO, query);
                            functionCacheVOS = functionCacheService.queryByEntity(queryFunctionCacheVO);
                            if (!CollectionUtils.isEmpty(functionCacheVOS)) {
                                functions = JSON.parseArray(JSONObject.toJSONString(functionCacheVOS), Function.class);
                            }
                        }
                    }
                }catch(Exception e)
                {
                    cacheIsRead = false;
                    logger.warn(e.getMessage(),e);
                }
                //从数据库查询
                if(CollectionUtils.isEmpty(functions)) {
                    functions = functionService.findListByEntity(query);
                    try{ //同步到缓存
                        if(!CollectionUtils.isEmpty(functions)&&cacheIsRead)
                        {
                            if(functionCacheService!=null) {
                                for (Function function : functions) {
                                    FunctionCacheVO functionCacheVO = new FunctionCacheVO();
                                    BeanUtils.copyProperties(functionCacheVO, function);
                                    functionCacheService.save(functionCacheVO);
                                }
                            }
                        }
                    }catch(Exception e)
                    {
                        cacheIsRead = false;
                        logger.warn(e.getMessage(),e);
                    }
                }
                if(!CollectionUtils.isEmpty(functions)) {

                    String[] roleIdArray = new String[adminRoles.size()];
                    int pos =0 ;
                    for(AdminRole adminRole:adminRoles)
                    {
                        roleIdArray[pos]=adminRole.getRoleId();
                        pos++;
                    }

                    resultObjectVO.setData(functionService.queryListByRoleIdArrayAndParentId(roleIdArray,String.valueOf(functions.get(0).getId())));
                }
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO list(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            FunctionTreeInfo queryPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), FunctionTreeInfo.class);


            //查询功能项列表
            PageInfo<Function> pageInfo =  functionService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询指定应用下所有功能项(不分页)
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListByAppCode(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            Function query = JSONObject.parseObject(requestVo.getEntityJson(), Function.class);
            query.setDeleteStatus((short) 0);
            List<Function> list = functionService.findListByEntity(query);
            resultObjectVO.setData(list);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
        }
        return resultObjectVO;
    }


    /**
     * 查询功能项详情
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryDetail(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            Function query = JSONObject.parseObject(requestVo.getEntityJson(), Function.class);
            Check.notNull(query.getId(), ResultVO.FAILD, "没有找到功能项ID");

            FunctionVO vo = functionService.findDetailById(query.getId());
            if (vo == null) {
                return ResultObjectVO.fail(ResultVO.FAILD, "功能项不存在!");
            }

            FunctionDetailVO detail = new FunctionDetailVO();
            detail.setBasicInfo(vo);
            detail.setAppName(vo.getAppName());
            resultObjectVO.setData(detail);

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


}
