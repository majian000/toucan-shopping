package com.toucan.shopping.cloud.apps.admin.controller.function;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.admin.auth.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.properties.Toucan;
import jakarta.servlet.http.HttpServletRequest;
import com.toucan.shopping.modules.admin.auth.entity.*;
import com.toucan.shopping.modules.admin.auth.vo.*;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FunctionPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private AppServiceAPI appServiceAPI;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/function/listPage", method = RequestMethod.GET)
    public String page(HttpServletRequest request) {
        //初始化选择应用控件
        super.initSelectApp(request, toucan, appServiceAPI);

        //初始化工具条按钮、操作按钮
        super.initButtons(request, toucan, "/function/listPage", functionServiceAPI);

        return "pages/function/list.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/function/addPage", method = RequestMethod.GET)
    public String addPage(HttpServletRequest request) {
        super.initSelectApp(request, toucan, appServiceAPI);


        return "pages/function/add.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/function/batchAddPage", method = RequestMethod.GET)
    public String batchAddPage(HttpServletRequest request) {
        super.initSelectApp(request, toucan, appServiceAPI);


        return "pages/function/batchAdd.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/function/editPage/{id}", method = RequestMethod.GET)
    public String editPage(HttpServletRequest request, @PathVariable Long id) {
        try {
            Function entity = new Function();
            entity.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            ResultObjectVO resultObjectVO = functionServiceAPI.findById(requestJsonVO);
            if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                if (resultObjectVO.getData() != null) {
                    List<Function> functions = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), Function.class);
                    if (!CollectionUtils.isEmpty(functions)) {
                        FunctionVO functionVO = new FunctionVO();
                        BeanUtils.copyProperties(functionVO, functions.get(0));
                        //如果是顶级节点,上级节点就是所属应用
                        if (functionVO.getPid().longValue() == -1) {
                            App queryApp = new App();
                            queryApp.setCode(functionVO.getAppCode());
                            requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryApp);
                            resultObjectVO = appServiceAPI.findByCode(requestJsonVO);
                            if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                                App app = JSONObject.parseObject(JSONObject.toJSONString(resultObjectVO.getData()), App.class);
                                if (app != null) {
                                    functionVO.setParentName(app.getCode() + " " + app.getName());
                                }
                            }
                        } else {
                            Function queryParentFunction = new Function();
                            queryParentFunction.setId(functionVO.getPid());
                            requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryParentFunction);
                            resultObjectVO = functionServiceAPI.findById(requestJsonVO);
                            if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                                List<Function> parentFunctionList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), Function.class);
                                if (!CollectionUtils.isEmpty(parentFunctionList)) {
                                    functionVO.setParentName(parentFunctionList.get(0).getName());
                                }
                            }
                        }
                        request.setAttribute("model", functionVO);
                    }
                }

            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return "pages/function/edit.html";
    }

}
