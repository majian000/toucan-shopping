package com.toucan.shopping.cloud.admin.auth.controller.function;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.*;
import com.toucan.shopping.modules.admin.auth.es.service.AdminRoleElasticSearchService;
import com.toucan.shopping.modules.admin.auth.es.service.FunctionElasticSearchService;
import com.toucan.shopping.modules.admin.auth.es.service.RoleFunctionElasticSearchService;
import com.toucan.shopping.modules.admin.auth.page.FunctionTreeInfo;
import com.toucan.shopping.modules.admin.auth.service.*;
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
import java.util.List;

/**
 * 功能项管理
 */
@RestController
@RequestMapping("/function")
public class FunctionController {


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

    @Autowired
    private FunctionElasticSearchService functionElasticSearchService;

    @Autowired
    private RoleFunctionElasticSearchService roleFunctionElasticSearchService;

    @Autowired
    private AdminRoleElasticSearchService adminRoleElasticSearchService;

    /**
     * 添加功能项
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
            Function entity = JSONObject.parseObject(requestVo.getEntityJson(),Function.class);
            if(StringUtils.isEmpty(entity.getName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("添加失败,请输入功能项名称");
                return resultObjectVO;
            }


            entity.setFunctionId(GlobalUUID.uuid());
            entity.setCreateDate(new Date());
            entity.setDeleteStatus((short)0);
            int row = functionService.save(entity);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("添加失败,请重试!");
                return resultObjectVO;
            }

            resultObjectVO.setData(entity);

            try {
                //同步es缓存,在这里要求缓存和数据库是一致的,如果缓存同步失败的话,数据库也会进行回滚
                FunctionElasticSearchVO functionElasticSearchVO = new FunctionElasticSearchVO();
                BeanUtils.copyProperties(functionElasticSearchVO, entity);
                functionElasticSearchService.save(functionElasticSearchVO);
            }catch(Exception e) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("更新缓存出现异常");
                logger.warn(e.getMessage(), e);
            }
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
    @RequestMapping(value = "/query/app/function/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAppFunctionTree(@RequestBody RequestJsonVO requestJsonVO)
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

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询指定用户和应用的权限树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/function/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryFunctionTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            App query = JSONObject.parseObject(requestJsonVO.getEntityJson(), App.class);
            //查询指定应用下的权限树
            resultObjectVO.setData(functionService.queryTreeByAppCode(query.getCode()));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 編輯功能项
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
            Function entity = JSONObject.parseObject(requestVo.getEntityJson(),Function.class);

            if(entity.getId().longValue()==entity.getPid().longValue())
            {
                logger.info("上级节点不能为自己 param:"+ JSONObject.toJSONString(entity));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("上级节点不能为自己!");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(entity.getName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请传入功能项名称");
                return resultObjectVO;
            }
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请传入功能项ID");
                return resultObjectVO;
            }


            Function query=new Function();
            query.setId(entity.getId());
            query.setDeleteStatus((short)0);
            List<Function> functions = functionService.findListByEntity(query);
            if(CollectionUtils.isEmpty(functions))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("该功能项不存在!");
                return resultObjectVO;
            }

            entity.setUpdateDate(new Date());
            int row = functionService.update(entity);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请重试!");
                return resultObjectVO;
            }


            try{
                //同步es缓存
                FunctionElasticSearchVO functionElasticSearchVO=new FunctionElasticSearchVO();
                BeanUtils.copyProperties(functionElasticSearchVO,entity);
                functionElasticSearchService.update(functionElasticSearchVO);
            }catch(Exception e) {
                resultObjectVO.setCode(ResultVO.FAILD);
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
                    List<String> deleteFaildIdList = new ArrayList<String>();
                    roleFunctionElasticSearchService.deleteByFunctionId(functions.get(0).getFunctionId(), deleteFaildIdList);
                }catch(Exception e) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("更新缓存出现异常");
                    logger.warn(e.getMessage(), e);
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
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/app/function/tree/table",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAppFunctionTreeTable(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

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
            Function entity = JSONObject.parseObject(requestVo.getEntityJson(),Function.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到功能项ID");
                return resultObjectVO;
            }

            //查询是否存在该功能项
            Function query=new Function();
            query.setId(entity.getId());
            List<Function> appList = functionService.findListByEntity(query);
            if(CollectionUtils.isEmpty(appList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,功能项不存在!");
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
     * 删除指定功能项
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
            Function entity = JSONObject.parseObject(requestVo.getEntityJson(),Function.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到功能项ID");
                return resultObjectVO;
            }

            List<Function> chidlren = new ArrayList<Function>();
            functionService.queryChildren(chidlren,entity);

            //把当前功能项添加进去,循环这个集合
            chidlren.add(entity);

            for(Function f:chidlren) {
                //删除当前功能项
                int row = functionService.deleteById(f.getId());
                if (row < 1) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请重试!");
                    continue;
                }

                //删除功能项下所有关联
                RoleFunction queryRoleFunction = new RoleFunction();
                queryRoleFunction.setFunctionId(f.getFunctionId());

                List<RoleFunction> roleFunction = roleFunctionService.findListByEntity(queryRoleFunction);
                if (!CollectionUtils.isEmpty(roleFunction)) {
                    row = roleFunctionService.deleteByFunctionId(f.getFunctionId());
                }


                try {
                    //刷新缓存
                    functionElasticSearchService.deleteById(String.valueOf(f.getId()));
                    if (!CollectionUtils.isEmpty(roleFunction)) {
                        List<String> deleteFaildIdList = new ArrayList<String>();
                        roleFunctionElasticSearchService.deleteByFunctionId(f.getFunctionId(), deleteFaildIdList);
                    }
                }catch(Exception e)
                {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("更新缓存出现异常");
                    logger.warn(e.getMessage(),e);
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
     * 批量删除功能项
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
            List<Function> functionList = JSONObject.parseArray(requestVo.getEntityJson(),Function.class);
            if(CollectionUtils.isEmpty(functionList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到功能项ID");
                return resultObjectVO;
            }
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
                            resultObjectVO.setMsg("请求失败,请重试!");
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
                            //刷新缓存
                            functionElasticSearchService.deleteById(String.valueOf(f.getId()));
                            if (!CollectionUtils.isEmpty(roleFunction)) {
                                List<String> deleteFaildIdList = new ArrayList<String>();
                                roleFunctionElasticSearchService.deleteByFunctionId(f.getFunctionId(),deleteFaildIdList);
                            }
                        }catch(Exception e)
                        {
                            resultObjectVO.setCode(ResultVO.FAILD);
                            resultObjectVO.setMsg("更新缓存出现异常");
                            logger.warn(e.getMessage(),e);
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




    /**
     * 查询指定管理员应用所有角色的功能项
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/admin/app/functions",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAdminAppFunctions(@RequestBody RequestJsonVO requestJsonVO)
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
                resultObjectVO.setData(functionService.queryListByRoleIdArray(roleIdArray));
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
     * 返回一级子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/one/children",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryChildren(@RequestBody RequestJsonVO requestJsonVO)
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
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
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
    @RequestMapping(value = "/query/admin/app/parent/url/one/child",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryOneChildsByAdminIdAndAppCodeAndParentUrl(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            //缓存是否可读取
            boolean cacheIsRead = true;
            FunctionVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), FunctionVO.class);
            AdminRoleElasticSearchVO queryAdminRoleElasticSearchVO = new AdminRoleElasticSearchVO();
            queryAdminRoleElasticSearchVO.setAdminId(query.getAdminId());
            queryAdminRoleElasticSearchVO.setAppCode(query.getAppCode());
            List<AdminRole> adminRoles = null;
            List<AdminRoleElasticSearchVO> adminRoleElasticSearchVOS = null;
            try {
                adminRoleElasticSearchVOS = adminRoleElasticSearchService.queryByEntity(queryAdminRoleElasticSearchVO);
                if(!CollectionUtils.isEmpty(adminRoleElasticSearchVOS))
                {
                    adminRoles = JSONObject.parseArray(JSONObject.toJSONString(adminRoleElasticSearchVOS),AdminRole.class);
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
                        for (AdminRole adminRole : adminRoles) {
                            AdminRoleElasticSearchVO adminRoleElasticSearchVO = new AdminRoleElasticSearchVO();
                            BeanUtils.copyProperties(adminRoleElasticSearchVO, adminRole);
                            adminRoleElasticSearchService.save(adminRoleElasticSearchVO);
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
                List<FunctionElasticSearchVO> functionElasticSearchVOS = null;
                try {
                    if(cacheIsRead) {
                        //从缓存查询数据
                        FunctionElasticSearchVO queryFunctionElasticSearchVO = new FunctionElasticSearchVO();
                        BeanUtils.copyProperties(queryFunctionElasticSearchVO, query);
                        functionElasticSearchVOS = functionElasticSearchService.queryByEntity(queryFunctionElasticSearchVO);
                        if (!CollectionUtils.isEmpty(functionElasticSearchVOS)) {
                            functions = JSONObject.parseArray(JSONObject.toJSONString(functionElasticSearchVOS), Function.class);
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
                            for (Function function : functions) {
                                FunctionElasticSearchVO functionElasticSearchVO = new FunctionElasticSearchVO();
                                BeanUtils.copyProperties(functionElasticSearchVO, function);
                                functionElasticSearchService.save(functionElasticSearchVO);
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
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO list(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            FunctionTreeInfo queryPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), FunctionTreeInfo.class);


            //查询功能项列表
            PageInfo<Function> pageInfo =  functionService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


}
