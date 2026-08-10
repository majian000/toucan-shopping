package com.toucan.shopping.cloud.apps.admin.controller;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private AdminServiceAPI adminServiceAPI;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void index(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect("/index/page");
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }


    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String page403(HttpServletRequest request, HttpServletResponse response) {
        return "403.html";
    }

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public String page404(HttpServletRequest request, HttpServletResponse response) {
        return "404.html";
    }

    @RequestMapping(value = "/500", method = RequestMethod.GET)
    public String page500(HttpServletRequest request, HttpServletResponse response) {
        return "500.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/index/page", method = RequestMethod.GET)
    public String page(HttpServletRequest request) {
        try {
            AdminVO adminVO = new AdminVO();
            adminVO.setAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), adminVO);
            ResultObjectVO resultObjectVO = adminServiceAPI.queryVOByEntity(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                adminVO = resultObjectVO.formatData(AdminVO.class);
                if (adminVO != null) {
                    request.setAttribute("model", adminVO);
                }
            }
        } catch (Exception e) {
            Admin admin = new Admin();
            admin.setUsername("");
            admin.setId(-1L);
            request.setAttribute("model", admin);
            logger.warn(e.getMessage(), e);
        }
        return "index.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/index/welcome", method = RequestMethod.GET)
    public String welcome(HttpServletRequest request) {
        try {
            FunctionVO function = new FunctionVO();
            function.setUrl("/index/welcome");
            function.setAppCode(toucan.getAppCode());
            function.setAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), function);
            ResultObjectVO resultObjectVO = functionServiceAPI.queryOneChildsByAdminIdAndAppCodeAndParentUrl(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<Function> functions = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), Function.class);
                if (!CollectionUtils.isEmpty(functions)) {
                    List<String> welcomeComponent = new ArrayList<String>();
                    StringBuilder welcomeComponentHtml = new StringBuilder();

                    for (Function buttonFunction : functions) {
                        if (buttonFunction.getType().shortValue() == 5) {
                            welcomeComponent.add(buttonFunction.getFunctionText());
                            welcomeComponentHtml.append(buttonFunction.getFunctionText());
                        }
                    }

                    request.setAttribute("welcomeComponentHtml", welcomeComponentHtml);
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            request.setAttribute("quickButtons", new ArrayList<String>());
        }

        return "welcome.html";
    }


    public void queryChild(List<FunctionVO> all, MenuInfo currentNode) {
        for (FunctionVO functionVO : all) {
            if (functionVO.getPid().longValue() == currentNode.getId().longValue()) {
                if (CollectionUtils.isEmpty(currentNode.getChild())) {
                    currentNode.setChild(new ArrayList<MenuInfo>());
                }
                if (functionVO.getType().shortValue() == 0 || functionVO.getType().shortValue() == 1) {
                    MenuInfo menuInfo = new MenuInfo();
                    menuInfo.setId(functionVO.getId());
                    menuInfo.setTitle(functionVO.getName());
                    menuInfo.setHref(functionVO.getUrl());
                    menuInfo.setTarget("_self");
                    menuInfo.setIcon(functionVO.getIcon());
                    currentNode.getChild().add(menuInfo);

                    queryChild(all, menuInfo);
                }
            }
        }
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:dashboard:menu"})
    @RequestMapping(value = "/index/menus", method = RequestMethod.GET)
    @ResponseBody
    public IndexInfo menus(HttpServletRequest request) {
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
            query.setAdminId(AdminLoginHolder.getCurrentAdminId());
            query.setAppCode(appCode);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), query);
            ResultObjectVO resultObjectVO = functionServiceAPI.queryAdminAppFunctions(requestJsonVO);
            if (resultObjectVO.getCode().longValue() == ResultObjectVO.SUCCESS.longValue()) {
                List<FunctionVO> functionVOList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), FunctionVO.class);
                if (functionVOList != null) {
                    for (FunctionVO functionVO : functionVOList) {
                        if (functionVO.getPid().longValue() == -1) {
                            if (functionVO.getType().shortValue() == 0 || functionVO.getType().shortValue() == 1) {
                                MenuInfo menuInfo = new MenuInfo();
                                menuInfo.setId(functionVO.getId());
                                menuInfo.setTitle(functionVO.getName());
                                menuInfo.setHref(functionVO.getUrl());
                                menuInfo.setTarget("_self");
                                menuInfo.setIcon(functionVO.getIcon());
                                menuInfos.add(menuInfo);

                                queryChild(functionVOList, menuInfo);
                            }
                        }
                    }
                }
            }
            indexInfo.setMenuInfo(menuInfos);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }

        return indexInfo;
    }


    /**
     * 查询当前登录用户信息(供Vue前端使用)
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/index/getInfo", method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO getInfo(HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AdminVO adminVO = new AdminVO();
            adminVO.setAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), adminVO);
            resultObjectVO = adminServiceAPI.queryVOByEntity(requestJsonVO);
            if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                AdminVO vo = resultObjectVO.formatData(AdminVO.class);
                if (vo != null) {
                    vo.setPassword(null);
                    resultObjectVO.setData(vo);
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询当前用户的权限标识列表(供Vue前端v-permission使用)
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/index/permissions", method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO permissions(HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AdminApp query = new AdminApp();
            query.setAdminId(AdminLoginHolder.getCurrentAdminId());
            query.setAppCode(appCode);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), query);
            ResultObjectVO functionsResult = functionServiceAPI.queryAdminAppFunctions(requestJsonVO);
            if (functionsResult.isSuccess()) {
                List<String> permissionList = new ArrayList<>();
                List<FunctionVO> functionVOList = JSONArray.parseArray(JSONObject.toJSONString(functionsResult.getData()), FunctionVO.class);
                if (functionVOList != null) {
                    for (FunctionVO f : functionVOList) {
                        if (f.getPermission() != null && !f.getPermission().isEmpty()) {
                            permissionList.add(f.getPermission());
                        }
                    }
                }
                resultObjectVO.setData(permissionList);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }

}
