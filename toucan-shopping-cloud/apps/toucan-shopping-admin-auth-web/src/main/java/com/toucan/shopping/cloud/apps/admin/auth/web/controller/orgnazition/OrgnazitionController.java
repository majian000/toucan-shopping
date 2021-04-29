package com.toucan.shopping.cloud.apps.admin.auth.web.controller.orgnazition;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.*;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.entity.Orgnazition;
import com.toucan.shopping.modules.admin.auth.page.OrgnazitionTreeInfo;
import com.toucan.shopping.modules.admin.auth.vo.OrgnazitionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.OrgnazitionVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    private FeignRoleService feignRoleService;



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String page(HttpServletRequest request)
    {
        //初始化选择应用控件
        super.initSelectApp(request,toucan,feignAppService);

        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/Orgnazition/listPage",feignFunctionService);

        return "pages/Orgnazition/list.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request)
    {
        super.initSelectApp(request,toucan,feignAppService);


        return "pages/Orgnazition/add.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            Orgnazition entity = new Orgnazition();
            entity.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            ResultObjectVO resultObjectVO = feignOrgnazitionService.findById(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    List<Orgnazition> Orgnazitions = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),Orgnazition.class);
                    if(!CollectionUtils.isEmpty(Orgnazitions))
                    {
                        OrgnazitionVO OrgnazitionVO = new OrgnazitionVO();
                        BeanUtils.copyProperties(OrgnazitionVO,Orgnazitions.get(0));
                        //如果是顶级节点,上级节点就是所属应用
                        if(OrgnazitionVO.getPid().longValue()==-1)
                        {
                            App queryApp = new App();
                            queryApp.setCode(OrgnazitionVO.getAppCode());
                            requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryApp);
                            resultObjectVO = feignAppService.findByCode(SignUtil.sign(requestJsonVO),requestJsonVO);
                            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue()) {
                                App app = JSONObject.parseObject(JSONObject.toJSONString(resultObjectVO.getData()),App.class);
                                if(app!=null) {
                                    OrgnazitionVO.setParentName(app.getCode()+" "+ app.getName());
                                }
                            }
                        }else{
                            Orgnazition queryParentOrgnazition = new Orgnazition();
                            queryParentOrgnazition.setId(OrgnazitionVO.getPid());
                            requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryParentOrgnazition);
                            resultObjectVO = feignOrgnazitionService.findById(SignUtil.sign(requestJsonVO),requestJsonVO);
                            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue()) {
                                List<Orgnazition> parentOrgnazitionList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),Orgnazition.class);
                                if(!CollectionUtils.isEmpty(parentOrgnazitionList)) {
                                    OrgnazitionVO.setParentName(parentOrgnazitionList.get(0).getName());
                                }
                            }
                        }
                        request.setAttribute("model",OrgnazitionVO);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/Orgnazition/edit.html";
    }



    /**
     * 修改
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request,@RequestBody Orgnazition entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
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
    public ResultObjectVO save(HttpServletRequest request, @RequestBody Orgnazition entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setCreateAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
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
    public ResultObjectVO listPage(HttpServletRequest request, OrgnazitionTreeInfo queryPageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            queryPageInfo.setAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
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
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));

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



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/query/app/Orgnazition/tree",method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO queryAppOrgnazitionTree(HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            App query = new App();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,query);
            return feignOrgnazitionService.queryAppOrgnazitionTree(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    public void setTreeNodeSelect(AtomicLong id,OrgnazitionTreeVO parentTreeVO,List<OrgnazitionTreeVO> OrgnazitionTreeVOList,List<RoleOrgnazition> roleOrgnazitions)
    {
        for(OrgnazitionTreeVO OrgnazitionTreeVO:OrgnazitionTreeVOList)
        {
            OrgnazitionTreeVO.setId(id.incrementAndGet());
            OrgnazitionTreeVO.setNodeId(OrgnazitionTreeVO.getId());
            OrgnazitionTreeVO.setPid(parentTreeVO.getId());
            OrgnazitionTreeVO.setParentId(OrgnazitionTreeVO.getPid());
            for(RoleOrgnazition roleOrgnazition:roleOrgnazitions) {
                if(OrgnazitionTreeVO.getOrgnazitionId().equals(roleOrgnazition.getOrgnazitionId())) {
                    //设置节点被选中
                    OrgnazitionTreeVO.getState().setChecked(true);
                }
            }
            if(!CollectionUtils.isEmpty(OrgnazitionTreeVO.getChildren()))
            {
                setTreeNodeSelect(id,OrgnazitionTreeVO,OrgnazitionTreeVO.getChildren(),roleOrgnazitions);
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
    @RequestMapping(value = "/query/role/Orgnazition/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryOrgnazitionTree(HttpServletRequest request,String appCode,String roleId)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            //查询权限树
            App query = new App();
            query.setCode(appCode);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),query);
            resultObjectVO = feignOrgnazitionService.queryOrgnazitionTree(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<OrgnazitionTreeVO> OrgnazitionTreeVOList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), OrgnazitionTreeVO.class);

                //重新设置ID,由于这个树是多个表合并而成,可能会存在ID重复
                AtomicLong id = new AtomicLong();
                RoleOrgnazition queryRoleOrgnazition = new RoleOrgnazition();
                queryRoleOrgnazition.setRoleId(roleId);
                requestJsonVO = RequestJsonVOGenerator.generator(appCode,queryRoleOrgnazition);
                resultObjectVO = feignRoleOrgnazitionService.queryRoleOrgnazitionList(SignUtil.sign(requestJsonVO),requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    List<RoleOrgnazition> roleOrgnazitions = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), RoleOrgnazition.class);
                    if(!CollectionUtils.isEmpty(roleOrgnazitions)) {
                        for(OrgnazitionTreeVO OrgnazitionTreeVO:OrgnazitionTreeVOList) {
                            OrgnazitionTreeVO.setId(id.incrementAndGet());
                            OrgnazitionTreeVO.setNodeId(OrgnazitionTreeVO.getId());
                            OrgnazitionTreeVO.setText(OrgnazitionTreeVO.getTitle());
                            for(RoleOrgnazition roleOrgnazition:roleOrgnazitions) {
                                if(OrgnazitionTreeVO.getOrgnazitionId().equals(roleOrgnazition.getOrgnazitionId())) {
                                    //设置节点被选中
                                    OrgnazitionTreeVO.getState().setChecked(true);
                                }
                            }
                            setTreeNodeSelect(id,OrgnazitionTreeVO,OrgnazitionTreeVO.getChildren(), roleOrgnazitions);
                        }
                    }
                }
                resultObjectVO.setData(OrgnazitionTreeVOList);
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

