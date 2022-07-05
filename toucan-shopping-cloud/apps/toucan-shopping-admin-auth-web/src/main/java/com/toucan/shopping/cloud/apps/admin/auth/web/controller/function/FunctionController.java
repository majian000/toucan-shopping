package com.toucan.shopping.cloud.apps.admin.auth.web.controller.function;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.*;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.modules.admin.auth.vo.AppFunctionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.RoleFunctionVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import com.toucan.shopping.modules.admin.auth.page.FunctionTreeInfo;
import com.toucan.shopping.modules.admin.auth.vo.FunctionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 功能项控制器
 */
@Controller
@RequestMapping("/function")
public class FunctionController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignAdminAppService feignAdminAppService;

    @Autowired
    private FeignAppService feignAppService;

    @Autowired
    private FeignRoleFunctionService feignRoleFunctionService;

    @Autowired
    private FeignRoleService feignRoleService;
    @Autowired
    private IdGenerator idGenerator;



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String page(HttpServletRequest request)
    {
        //初始化选择应用控件
        super.initSelectApp(request,toucan,feignAppService);

        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/function/listPage",feignFunctionService);

        return "pages/function/list.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request)
    {
        super.initSelectApp(request,toucan,feignAppService);


        return "pages/function/add.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            Function entity = new Function();
            entity.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            ResultObjectVO resultObjectVO = feignFunctionService.findById(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    List<Function> functions = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),Function.class);
                    if(!CollectionUtils.isEmpty(functions))
                    {
                        FunctionVO functionVO = new FunctionVO();
                        BeanUtils.copyProperties(functionVO,functions.get(0));
                        //如果是顶级节点,上级节点就是所属应用
                        if(functionVO.getPid().longValue()==-1)
                        {
                            App queryApp = new App();
                            queryApp.setCode(functionVO.getAppCode());
                            requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryApp);
                            resultObjectVO = feignAppService.findByCode(SignUtil.sign(requestJsonVO),requestJsonVO);
                            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue()) {
                                App app = JSONObject.parseObject(JSONObject.toJSONString(resultObjectVO.getData()),App.class);
                                if(app!=null) {
                                    functionVO.setParentName(app.getCode()+" "+ app.getName());
                                }
                            }
                        }else{
                            Function queryParentFunction = new Function();
                            queryParentFunction.setId(functionVO.getPid());
                            requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryParentFunction);
                            resultObjectVO = feignFunctionService.findById(SignUtil.sign(requestJsonVO),requestJsonVO);
                            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue()) {
                                List<Function> parentFunctionList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),Function.class);
                                if(!CollectionUtils.isEmpty(parentFunctionList)) {
                                    functionVO.setParentName(parentFunctionList.get(0).getName());
                                }
                            }
                        }
                        request.setAttribute("model",functionVO);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/function/edit.html";
    }



    /**
     * 修改
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request,@RequestBody Function entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignFunctionService.update(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 保存
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request, @RequestBody Function entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setCreateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignFunctionService.save(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 查询列表
     * @param queryPageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/tree/table",method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO treeTable(HttpServletRequest request, FunctionTreeInfo queryPageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            queryPageInfo.setAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryPageInfo);
            resultObjectVO = feignFunctionService.queryAppFunctionTreeTable(SignUtil.sign(requestJsonVO),requestJsonVO);
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 查询列表
     * @param queryPageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/tree/table/by/pid",method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO treeTableByPid(HttpServletRequest request, FunctionTreeInfo queryPageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            queryPageInfo.setAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryPageInfo);
            resultObjectVO = feignFunctionService.queryAppFunctionTreeTableByPid(SignUtil.sign(requestJsonVO),requestJsonVO);
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 删除功能项
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(HttpServletRequest request,  @PathVariable String id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            Function entity =new Function();
            entity.setId(Long.parseLong(id));
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));

            String entityJson = JSONObject.toJSONString(entity);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignFunctionService.deleteById(SignUtil.sign(requestVo),requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 清空该应用下所有功能项
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/delete/by/app/code/{appCode}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByAppCode(HttpServletRequest request,  @PathVariable String appCode)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(appCode))
            {
                resultObjectVO.setMsg("请传入应用编码");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            Function entity =new Function();
            entity.setAppCode(appCode);
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));

            String entityJson = JSONObject.toJSONString(entity);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignFunctionService.deleteByAppCode(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 删除应用
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/delete/ids",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<FunctionVO> functionVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(functionVOS))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(functionVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignFunctionService.deleteByIds(SignUtil.sign(requestVo), requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/query/app/function/tree")
    @ResponseBody
    public ResultObjectVO queryAppFunctionTree(HttpServletRequest request,FunctionTreeVO functionTreeVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            //默认查询根节点
            if(functionTreeVO.getId()==null)
            {
                App query = new App();
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,query);
                resultObjectVO = feignAppService.list(appCode,requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    List<App> rootNodes = resultObjectVO.formatDataList(App.class);
                    List<AppFunctionTreeVO> appFunctionTreeVOS = new LinkedList<>();
                    for(App rootNode:rootNodes) {
                        //随机生成一个应用节点ID,必须是负数
                        AppFunctionTreeVO appFunctionTreeVO = new AppFunctionTreeVO();
                        appFunctionTreeVO.setId((0-idGenerator.id()));
                        appFunctionTreeVO.setPid(-2L);
                        appFunctionTreeVO.setParentId(-2L);
                        appFunctionTreeVO.setAppCode(rootNode.getCode());
                        appFunctionTreeVO.setTitle(rootNode.getCode() + " " + rootNode.getName());
                        appFunctionTreeVO.setName(rootNode.getCode() + " " + rootNode.getName());
                        appFunctionTreeVO.setEnableStatus((short) 1);
                        appFunctionTreeVO.setIsParent(true);
                        appFunctionTreeVO.setIsAppNode(true); //是应用节点
                        appFunctionTreeVOS.add(appFunctionTreeVO);
                    }
                    resultObjectVO.setData(appFunctionTreeVOS);
                }
            }else{
                //如果展开的是应用下面的第一级节点
                if(functionTreeVO.getIsAppNode()!=null&&functionTreeVO.getIsAppNode().booleanValue()==true)
                {
                    functionTreeVO.setParentId(-1L);
                }else { //就查询这个节点下的子节点(当前选择节点深度大于1)
                    functionTreeVO.setParentId(functionTreeVO.getId());
                }
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,functionTreeVO);
                resultObjectVO = feignFunctionService.queryAppFunctionTreeByPid(requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    List<FunctionTreeVO> functionTreeVOS = resultObjectVO.formatDataList(FunctionTreeVO.class);
                    for(FunctionTreeVO tree:functionTreeVOS)
                    {
                        //手动设置第一级节点的父节点ID为应用编码
                        if(String.valueOf(functionTreeVO.getId()).equals(functionTreeVO.getAppCode()))
                        {
                            tree.setParentId(functionTreeVO.getId());
                        }
                        tree.setIsAppNode(false);
                    }
                    resultObjectVO.setData(functionTreeVOS);
                }
            }
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    public void setTreeNodeSelect(AtomicLong id,FunctionTreeVO parentTreeVO,List<FunctionTreeVO> functionTreeVOList,List<RoleFunction> roleFunctions)
    {
        for(FunctionTreeVO functionTreeVO:functionTreeVOList)
        {
            functionTreeVO.setId(id.incrementAndGet());
            functionTreeVO.setNodeId(functionTreeVO.getId());
            functionTreeVO.setPid(parentTreeVO.getId());
            functionTreeVO.setParentId(functionTreeVO.getPid());
            for(RoleFunction roleFunction:roleFunctions) {
                if(functionTreeVO.getFunctionId().equals(roleFunction.getFunctionId())) {
                    //设置节点被选中
                    functionTreeVO.getState().setChecked(true);
                }
            }
            if(!CollectionUtils.isEmpty(functionTreeVO.getChildren()))
            {
                setTreeNodeSelect(id,functionTreeVO,functionTreeVO.getChildren(),roleFunctions);
            }
        }
    }


    /**
     * 返回指定角色下的功能树
     * @param request
     * @param appCode
     * @param roleId
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/query/role/function/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryFunctionTree(HttpServletRequest request,String appCode,String roleId)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            //查询权限树
            App query = new App();
            query.setCode(appCode);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),query);
            resultObjectVO = feignFunctionService.queryFunctionTree(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<FunctionTreeVO> functionTreeVOList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), FunctionTreeVO.class);

                //重新设置ID,由于这个树是多个表合并而成,可能会存在ID重复
                AtomicLong id = new AtomicLong();
                RoleFunction queryRoleFunction = new RoleFunction();
                queryRoleFunction.setRoleId(roleId);
                requestJsonVO = RequestJsonVOGenerator.generator(appCode,queryRoleFunction);
                resultObjectVO = feignRoleFunctionService.queryRoleFunctionList(SignUtil.sign(requestJsonVO),requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    List<RoleFunction> roleFunctions = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), RoleFunction.class);
                    if(!CollectionUtils.isEmpty(roleFunctions)) {
                        for(FunctionTreeVO functionTreeVO:functionTreeVOList) {
                            functionTreeVO.setId(id.incrementAndGet());
                            functionTreeVO.setNodeId(functionTreeVO.getId());
                            functionTreeVO.setText(functionTreeVO.getTitle());
                            for(RoleFunction roleFunction:roleFunctions) {
                                if(functionTreeVO.getFunctionId().equals(roleFunction.getFunctionId())) {
                                    //设置节点被选中
                                    functionTreeVO.getState().setChecked(true);
                                }
                            }
                            setTreeNodeSelect(id,functionTreeVO,functionTreeVO.getChildren(), roleFunctions);
                        }
                    }
                }
                resultObjectVO.setData(functionTreeVOList);
            }
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





    /**
     * 返回指定角色下的功能树
     * @param request
     * @param appCode
     * @param roleId
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/query/role/function/tree/{appCode}/{roleId}",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryRoleFunctionTree(HttpServletRequest request,@PathVariable String appCode,@PathVariable String roleId,@RequestParam String id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                id="-1";
            }
            //查询权限树
            RoleFunctionVO roleFunctionVO = new RoleFunctionVO();
            roleFunctionVO.setPid(Long.parseLong(id));
            roleFunctionVO.setRoleId(roleId);
            roleFunctionVO.setAppCode(appCode);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),roleFunctionVO);
            resultObjectVO = feignRoleFunctionService.queryFunctionTreeByRoleIdAndParentId(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<FunctionTreeVO> fucntionTreeVOS = resultObjectVO.formatDataList(FunctionTreeVO.class);
                if(!CollectionUtils.isEmpty(fucntionTreeVOS))
                {
                    for(FunctionTreeVO functionTreeVO:fucntionTreeVOS)
                    {
                        functionTreeVO.setUrl(null);
                    }
                }
                resultObjectVO.setData(fucntionTreeVOS);
            }
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

}

