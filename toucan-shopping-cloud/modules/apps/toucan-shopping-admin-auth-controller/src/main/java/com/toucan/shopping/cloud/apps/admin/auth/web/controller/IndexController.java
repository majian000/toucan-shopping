package com.toucan.shopping.cloud.apps.admin.auth.web.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminAppService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.HomeInfo;
import com.toucan.shopping.modules.layui.vo.IndexInfo;
import com.toucan.shopping.modules.layui.vo.LogoInfo;
import com.toucan.shopping.modules.layui.vo.MenuInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignAdminService feignAdminService;


    @RequestMapping(value = "/",method = RequestMethod.GET)
    public void index(HttpServletRequest request, HttpServletResponse response)
    {
        try {
            response.sendRedirect("/index/page");
        } catch (IOException e) {
            logger.warn(e.getMessage(),e);
        }
    }


    @RequestMapping(value = "/403",method = RequestMethod.GET)
    public String page403(HttpServletRequest request, HttpServletResponse response)
    {
        return "/403.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/index/page",method = RequestMethod.GET)
    public String page(HttpServletRequest request)
    {
        try {
            AdminVO adminVO = new AdminVO();
            adminVO.setAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),adminVO);
            ResultObjectVO resultObjectVO = feignAdminService.queryListByEntity(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                List<Admin> admins = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),Admin.class);
                if (!CollectionUtils.isEmpty(admins)) {
                    request.setAttribute("model",admins.get(0));
                }
            }
        }catch(Exception e)
        {
            Admin admin = new Admin();
            admin.setUsername("");
            admin.setId(-1L);
            request.setAttribute("model",admin);
            logger.warn(e.getMessage(),e);
        }
        return "index.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/index/welcome",method = RequestMethod.GET)
    public String welcome(HttpServletRequest request)
    {
        try {
            FunctionVO function = new FunctionVO();
            function.setUrl("/index/welcome");
            function.setAppCode(toucan.getAppCode());
            function.setAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),function);
            ResultObjectVO resultObjectVO = feignFunctionService.queryOneChildsByAdminIdAndAppCodeAndParentUrl(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<Function> functions = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),Function.class);
                if(!CollectionUtils.isEmpty(functions))
                {
                    List<String> quickButtons = new ArrayList<String>();

                    for(Function buttonFunction:functions)
                    {
                        if(buttonFunction.getType().shortValue()==5)
                        {
                            quickButtons.add(buttonFunction.getFunctionText());
                        }
                    }

                    request.setAttribute("quickButtons",quickButtons);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            request.setAttribute("quickButtons",new ArrayList<String>());
        }

        return "welcome.html";
    }


    /**
     * 查询出每个节点的子节点
     * @param all
     * @param currentNode
     */
    public void queryChild(List<FunctionVO> all,MenuInfo currentNode)
    {
        for(FunctionVO functionVO:all)
        {
            if(functionVO.getPid().longValue()==currentNode.getId().longValue())
            {
                if(CollectionUtils.isEmpty(currentNode.getChild()))
                {
                    currentNode.setChild(new ArrayList<MenuInfo>());
                }
                //只查询目录和菜单
                if(functionVO.getType().shortValue()==0||functionVO.getType().shortValue()==1) {
                    MenuInfo menuInfo=new MenuInfo();
                    menuInfo.setId(functionVO.getId());
                    menuInfo.setTitle(functionVO.getName());
                    menuInfo.setHref(functionVO.getUrl());
                    menuInfo.setTarget("_self");
                    menuInfo.setIcon(functionVO.getIcon());
                    currentNode.getChild().add(menuInfo);

                    //查询子节点
                    queryChild(all,menuInfo);
                }
            }
        }
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/index/menus",method = RequestMethod.GET)
    @ResponseBody
    public IndexInfo menus(HttpServletRequest request)
    {
        IndexInfo indexInfo = new IndexInfo();

        HomeInfo homeInfo = new HomeInfo();
        homeInfo.setTitle("首页");
        homeInfo.setHref("welcome");
        indexInfo.setHomeInfo(homeInfo);

        LogoInfo logoInfo = new LogoInfo();
        logoInfo.setTitle(toucan.getAppName());
        logoInfo.setImage("../images/logo.png");
        logoInfo.setHref("");
        indexInfo.setLogoInfo(logoInfo);

        List<MenuInfo> menuInfos = new ArrayList<MenuInfo>();

        try {
            AdminApp query = new AdminApp();
            query.setAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            query.setAppCode(appCode);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),query);
            ResultObjectVO resultObjectVO = feignFunctionService.queryAdminAppFunctions(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.getCode().longValue()==ResultObjectVO.SUCCESS.longValue())
            {
                List<FunctionVO> functionVOList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),FunctionVO.class);
                for(FunctionVO functionVO:functionVOList)
                {
                    if(functionVO.getPid().longValue()==-1)
                    {
                        //只查询目录和菜单
                        if(functionVO.getType().shortValue()==0||functionVO.getType().shortValue()==1) {
                            MenuInfo menuInfo=new MenuInfo();
                            menuInfo.setId(functionVO.getId());
                            menuInfo.setTitle(functionVO.getName());
                            menuInfo.setHref(functionVO.getUrl());
                            menuInfo.setTarget("_self");
                            menuInfo.setIcon(functionVO.getIcon());
                            menuInfos.add(menuInfo);

                            //查询子节点
                            queryChild(functionVOList,menuInfo);
                        }
                    }
                }
            }
            indexInfo.setMenuInfo(menuInfos);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }

        return indexInfo;
    }

}

