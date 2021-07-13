package com.toucan.shopping.cloud.apps.admin.auth.web.controller.orgnazition;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.*;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.modules.admin.auth.entity.AdminOrgnazition;
import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.entity.Orgnazition;
import com.toucan.shopping.modules.admin.auth.entity.OrgnazitionApp;
import com.toucan.shopping.modules.admin.auth.page.OrgnazitionTreeInfo;
import com.toucan.shopping.modules.admin.auth.vo.*;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 组织机构控制器
 */
@Controller
@RequestMapping("/orgnazition")
public class OrgnazitionController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignOrgnazitionService feignOrgnazitionService;

    @Autowired
    private FeignAdminAppService feignAdminAppService;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignAppService feignAppService;

    @Autowired
    private FeignAdminOrgnazitionService feignAdminOrgnazitionService;




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String page(HttpServletRequest request)
    {
        //初始化选择应用控件
        super.initSelectApp(request,toucan,feignAppService);

        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/orgnazition/listPage",feignFunctionService);

        return "pages/orgnazition/list.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request)
    {
        super.initSelectApp(request,toucan,feignAppService);


        return "pages/orgnazition/add.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {

            super.initSelectApp(request,toucan,feignAppService);

            Orgnazition entity = new Orgnazition();
            entity.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            ResultObjectVO resultObjectVO = feignOrgnazitionService.findById(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    List<OrgnazitionVO> orgnazitions = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),OrgnazitionVO.class);
                    if(!CollectionUtils.isEmpty(orgnazitions))
                    {
                        //查询上级机构名称
                        OrgnazitionVO orgnazitionVO = new OrgnazitionVO();
                        BeanUtils.copyProperties(orgnazitionVO,orgnazitions.get(0));
                        Orgnazition queryParentOrgnazition = new Orgnazition();
                        queryParentOrgnazition.setId(orgnazitionVO.getPid());
                        requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryParentOrgnazition);
                        resultObjectVO = feignOrgnazitionService.findById(SignUtil.sign(requestJsonVO),requestJsonVO);
                        if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue()) {
                            List<Orgnazition> parentOrgnazitionList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),Orgnazition.class);
                            if(!CollectionUtils.isEmpty(parentOrgnazitionList)) {
                                orgnazitionVO.setParentName(parentOrgnazitionList.get(0).getName());
                            }
                        }

                        //设置关联应用选中
                        Object appsObject = request.getAttribute("apps");
                        if(appsObject!=null) {
                            List<AppVO> appVos = (List<AppVO>) appsObject;
                            if(!CollectionUtils.isEmpty(orgnazitionVO.getOrgnazitionApps()))
                            {
                                for(OrgnazitionApp orgnazitionApp:orgnazitionVO.getOrgnazitionApps())
                                {
                                    for(AppVO aa:appVos)
                                    {
                                        if(orgnazitionApp.getAppCode().equals(aa.getCode()))
                                        {
                                            aa.setChecked(true);
                                        }
                                    }
                                }
                            }
                        }

                        request.setAttribute("model",orgnazitionVO);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/orgnazition/edit.html";
    }



    /**
     * 修改
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request,@RequestBody OrgnazitionVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            List<String> appCodes = new ArrayList();
            appCodes.add(toucan.getAppCode());
            entity.setAppCodes(appCodes);
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignOrgnazitionService.update(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
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
    public ResultObjectVO save(HttpServletRequest request, @RequestBody OrgnazitionVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            List<String> appCodes = new ArrayList();
            appCodes.add(toucan.getAppCode());
            entity.setAppCodes(appCodes);
            entity.setCreateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignOrgnazitionService.save(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
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
    public ResultObjectVO treeTable(HttpServletRequest request, OrgnazitionTreeInfo queryPageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            queryPageInfo.setAppCode(toucan.getAppCode());
            queryPageInfo.setAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryPageInfo);
            resultObjectVO = feignOrgnazitionService.queryAppOrgnazitionTreeTable(SignUtil.sign(requestJsonVO),requestJsonVO);
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
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
                resultObjectVO.setMsg("请求失败,请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            Orgnazition entity =new Orgnazition();
            entity.setId(Long.parseLong(id));
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));

            String entityJson = JSONObject.toJSONString(entity);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignOrgnazitionService.deleteById(SignUtil.sign(requestVo),requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/query/tree",method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO queryOrgnazitionTree(HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            App query = new App();
            query.setCode(toucan.getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,query);
            return feignOrgnazitionService.queryOrgnazationTree(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    public void setTreeNodeSelect(AtomicLong id,OrgnazitionTreeVO parentTree,List<OrgnazitionTreeVO> orgnazitionTreeVOS,List<AdminOrgnazition> adminOrgnazitions)
    {
        for(OrgnazitionTreeVO orgnazitionTreeVO:orgnazitionTreeVOS)
        {
            orgnazitionTreeVO.setId(id.incrementAndGet());
            orgnazitionTreeVO.setNodeId(orgnazitionTreeVO.getId());
            orgnazitionTreeVO.setParentId(parentTree.getId());
            for(AdminOrgnazition adminOrgnazition:adminOrgnazitions) {
                if(adminOrgnazition.getOrgnazitionId().equals(orgnazitionTreeVO.getOrgnazitionId())) {
                    orgnazitionTreeVO.getState().setChecked(true);
                }
            }
            if(!CollectionUtils.isEmpty(orgnazitionTreeVO.getChildren()))
            {
                setTreeNodeSelect(id,orgnazitionTreeVO,orgnazitionTreeVO.getChildren(),adminOrgnazitions);
            }
        }
    }




    /**
     * 查看账号所关联应用的所有组织机构树
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/query/admin/orgnazition/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAdminOrgnazitionTree(HttpServletRequest request,@RequestBody AdminAppVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            //查询对应账户的应用
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),entity);
            resultObjectVO = feignOrgnazitionService.queryAdminOrgnazitionTree(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                //拿到组织机构树
                List<OrgnazitionTreeVO> orgnazitionTreeVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), OrgnazitionTreeVO.class);

                //重新设置ID,由于这个树是多个表合并而成,可能会存在ID重复,layui不支持id重复
                AtomicLong id = new AtomicLong();
                //查询要操作账户的所有组织机构关联
                AdminOrgnazition queryAdminOrgnazition = new AdminOrgnazition();
                queryAdminOrgnazition.setAdminId(entity.getAdminId());
                requestJsonVO = RequestJsonVOGenerator.generator(appCode,queryAdminOrgnazition);
                resultObjectVO = feignAdminOrgnazitionService.queryListByEntity(SignUtil.sign(requestJsonVO),requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    List<AdminOrgnazition> adminOrgnazitionList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), AdminOrgnazition.class);
                    if(!CollectionUtils.isEmpty(adminOrgnazitionList)) {
                        for(OrgnazitionTreeVO orgnazitionTreeVO:orgnazitionTreeVOS) {
                            orgnazitionTreeVO.setId(id.incrementAndGet());
                            orgnazitionTreeVO.setNodeId(orgnazitionTreeVO.getId());
                            for(AdminOrgnazition adminOrgnazition:adminOrgnazitionList) {
                                if(orgnazitionTreeVO.getOrgnazitionId().equals(adminOrgnazition.getOrgnazitionId())) {
                                    //设置节点被选中
                                    orgnazitionTreeVO.getState().setChecked(true);
                                }
                            }
                            setTreeNodeSelect(id,orgnazitionTreeVO,orgnazitionTreeVO.getChildren(), adminOrgnazitionList);
                        }
                    }
                }
                resultObjectVO.setData(orgnazitionTreeVOS);
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
     * 删除应用
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/delete/ids",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<OrgnazitionVO> OrgnazitionVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(OrgnazitionVOS))
            {
                resultObjectVO.setMsg("请求失败,请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(OrgnazitionVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignOrgnazitionService.deleteByIds(SignUtil.sign(requestVo), requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





}

