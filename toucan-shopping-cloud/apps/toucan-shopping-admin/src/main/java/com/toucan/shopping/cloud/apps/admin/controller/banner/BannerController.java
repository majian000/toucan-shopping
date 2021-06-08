package com.toucan.shopping.cloud.apps.admin.controller.banner;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.*;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.area.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.area.api.feign.service.FeignBannerService;
import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.page.AdminPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.*;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.area.vo.BannerVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
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
import java.util.Map;

/**
 * 轮播图管理
 */
@Controller
@RequestMapping("/banner")
public class BannerController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;


    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private FeignBannerService feignBannerService;

    @Autowired
    private FeignAreaService feignAreaService;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/banner/listPage",feignFunctionService);
        return "pages/banner/list.html";
    }



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, AdminPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = feignBannerService.queryList(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    if(tableVO.getCount()>0) {
                        tableVO.setData((List<Object>) resultObjectDataMap.get("list"));
                    }
                }
            }
        }catch(Exception e)
        {
            tableVO.setMsg("请求失败,请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }


    /**
     * 保存
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request, @RequestBody BannerVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setId(idGenerator.id());
            entity.setCreateAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignBannerService.save(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/query/area/tree",method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO queryTree(HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AreaVO query = new AreaVO();
            //设置为商城的应用编码
            query.setAppCode("10001001");
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,query);
            return feignAreaService.queryTree(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request)
    {

        return "pages/banner/add.html";
    }



}

