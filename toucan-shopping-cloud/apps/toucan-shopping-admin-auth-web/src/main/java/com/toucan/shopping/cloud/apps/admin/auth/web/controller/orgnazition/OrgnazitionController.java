package com.toucan.shopping.cloud.apps.admin.auth.web.controller.orgnazition;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.*;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.modules.admin.auth.entity.AdminOrgnazition;
import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.entity.Orgnazition;
import com.toucan.shopping.modules.admin.auth.entity.OrgnazitionApp;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
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

import jakarta.servlet.http.HttpServletRequest;
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
    private OrgnazitionServiceAPI orgnazitionServiceAPI;

    @Autowired
    private AdminAppServiceAPI adminAppServiceAPI;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private AppServiceAPI appServiceAPI;

    @Autowired
    private AdminOrgnazitionServiceAPI adminOrgnazitionServiceAPI;













    /**
     * 修改
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:org:update"})
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request,@RequestBody OrgnazitionVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = orgnazitionServiceAPI.update(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:org:save"})
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request, @RequestBody OrgnazitionVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = orgnazitionServiceAPI.save(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:org:list"})
    @RequestMapping(value = "/tree/table",method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO treeTable(HttpServletRequest request, OrgnazitionTreeInfo queryPageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            queryPageInfo.setAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryPageInfo);
            resultObjectVO = orgnazitionServiceAPI.queryAppOrgnazitionTreeTable(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:org:delete"})
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteById(HttpServletRequest request, @RequestBody Orgnazition orgnazition)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(orgnazition.getId() == null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            Orgnazition entity =new Orgnazition();
            // id from @RequestBody
                        String entityJson = JSONObject.toJSONString(orgnazition);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = orgnazitionServiceAPI.deleteById(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_JSON,responseType=AdminAuth.RESPONSE_JSON)
    @RequestMapping(value = "/query/tree",method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO queryOrgnazitionTree(HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            App query = new App();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,query);
            return orgnazitionServiceAPI.queryOrgnazationTree(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_JSON,responseType=AdminAuth.RESPONSE_JSON)
    @RequestMapping(value = "/query/admin/orgnazition/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAdminOrgnazitionTree(HttpServletRequest request,@RequestBody AdminAppVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            //查询对应账户的应用
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),entity);
            resultObjectVO = orgnazitionServiceAPI.queryAdminOrgnazitionTree(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                //拿到组织机构树
                List<OrgnazitionTreeVO> orgnazitionTreeVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), OrgnazitionTreeVO.class);

                //重新设置ID,由于这个树是多个表合并而成,可能会存在ID重复,layui不支持id重复
                AtomicLong id = new AtomicLong();
                //查询要操作账户的所有组织机构关联
                AdminOrgnazition queryAdminOrgnazition = new AdminOrgnazition();
                queryAdminOrgnazition.setAdminId(entity.getAdminId());
                queryAdminOrgnazition.setAppCode(entity.getAppCode());
                requestJsonVO = RequestJsonVOGenerator.generator(appCode,queryAdminOrgnazition);
                resultObjectVO = adminOrgnazitionServiceAPI.queryListByEntity(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:org:batch-delete-api"})
    @RequestMapping(value = "/delete/ids",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<OrgnazitionVO> OrgnazitionVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(OrgnazitionVOS))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(OrgnazitionVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = orgnazitionServiceAPI.deleteByIds( requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





}

