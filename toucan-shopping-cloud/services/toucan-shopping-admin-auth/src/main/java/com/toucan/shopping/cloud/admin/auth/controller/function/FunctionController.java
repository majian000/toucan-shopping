package com.toucan.shopping.cloud.admin.auth.controller.function;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.*;
import com.toucan.shopping.modules.admin.auth.page.FunctionTreeInfo;
import com.toucan.shopping.modules.admin.auth.service.*;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.admin.auth.vo.AppFunctionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
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
            AdminApp query = JSONObject.parseObject(requestJsonVO.getEntityJson(), AdminApp.class);
            if(StringUtils.isEmpty(query.getAdminId()))
            {
                throw new IllegalArgumentException("adminId为空");
            }

            //当前用户下关联所有应用
            List<AdminAppVO> adminApps = adminAppService.findAppListByAdminAppEntity(query);
            if(!CollectionUtils.isEmpty(adminApps))
            {
                List<AppFunctionTreeVO> appFunctionTreeVOS = new ArrayList<AppFunctionTreeVO>();
                for(AdminAppVO adminAppVO : adminApps)
                {
                    AppFunctionTreeVO appFunctionTreeVO = new AppFunctionTreeVO();
                    appFunctionTreeVO.setId(-1L);
                    appFunctionTreeVO.setAppCode(adminAppVO.getAppCode());
                    appFunctionTreeVO.setTitle(adminAppVO.getName());
                    appFunctionTreeVO.setEnableStatus((short)1);
                    appFunctionTreeVOS.add(appFunctionTreeVO);

                    appFunctionTreeVO.setChildren(functionService.queryTreeByAppCode(adminAppVO.getAppCode()));

                }
                resultObjectVO.setData(appFunctionTreeVOS);
            }

        }catch(Exception e)
        {
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
            List<Function> FunctionList = functionService.findListByEntity(query);
            if(CollectionUtils.isEmpty(FunctionList))
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
                    appFunctionTreeVO.setName(adminAppVO.getName());
                    appFunctionTreeVO.setPid(-1L);
                    appFunctionTreeVO.setEnableStatus((short)1);
                    appFunctionTreeVOS.add(appFunctionTreeVO);

                    //设置应用编码和功能项名称
                    Function queryFunction = new Function();
                    queryFunction.setAppCode(adminAppVO.getAppCode());
                    queryFunction.setName(queryPageInfo.getName());
                    List<Function>  functions = functionService.findListByEntity(queryFunction);
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
                    row = roleFunctionService.deleteByFunctionId(f.getId());

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
                            roleFunctionService.deleteByFunctionId(f.getId());
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
