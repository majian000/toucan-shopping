package com.toucan.shopping.cloud.apps.admin.controller.orgnazition;

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
public class OrgnazitionPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private AppServiceAPI appServiceAPI;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;



    @Autowired
    private OrgnazitionServiceAPI orgnazitionServiceAPI;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/orgnazition/listPage", method = RequestMethod.GET)
    public String page(HttpServletRequest request) {
        //初始化选择应用控件
        super.initSelectApp(request, toucan, appServiceAPI);

        //初始化工具条按钮、操作按钮
        super.initButtons(request, toucan, "/orgnazition/listPage", functionServiceAPI);

        return "pages/orgnazition/list.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/orgnazition/addPage", method = RequestMethod.GET)
    public String addPage(HttpServletRequest request) {
        super.initSelectApp(request, toucan, appServiceAPI);


        return "pages/orgnazition/add.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/orgnazition/editPage/{id}", method = RequestMethod.GET)
    public String editPage(HttpServletRequest request, @PathVariable Long id) {
        try {

            super.initSelectApp(request, toucan, appServiceAPI);

            Orgnazition entity = new Orgnazition();
            entity.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            ResultObjectVO resultObjectVO = orgnazitionServiceAPI.findById(requestJsonVO);
            if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                if (resultObjectVO.getData() != null) {
                    List<OrgnazitionVO> orgnazitions = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), OrgnazitionVO.class);
                    if (!CollectionUtils.isEmpty(orgnazitions)) {
                        //查询上级机构名称
                        OrgnazitionVO orgnazitionVO = new OrgnazitionVO();
                        BeanUtils.copyProperties(orgnazitionVO, orgnazitions.get(0));
                        Orgnazition queryParentOrgnazition = new Orgnazition();
                        queryParentOrgnazition.setId(orgnazitionVO.getPid());
                        requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryParentOrgnazition);
                        resultObjectVO = orgnazitionServiceAPI.findById(requestJsonVO);
                        if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                            List<Orgnazition> parentOrgnazitionList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), Orgnazition.class);
                            if (!CollectionUtils.isEmpty(parentOrgnazitionList)) {
                                orgnazitionVO.setParentName(parentOrgnazitionList.get(0).getName());
                            }
                        }

                        //设置关联应用选中
                        Object appsObject = request.getAttribute("apps");
                        if (appsObject != null) {
                            List<AppVO> appVos = (List<AppVO>) appsObject;
                            if (!CollectionUtils.isEmpty(orgnazitionVO.getOrgnazitionApps())) {
                                for (OrgnazitionApp orgnazitionApp : orgnazitionVO.getOrgnazitionApps()) {
                                    for (AppVO aa : appVos) {
                                        if (orgnazitionApp.getAppCode().equals(aa.getCode())) {
                                            aa.setChecked(true);
                                        }
                                    }
                                }
                            }
                        }

                        request.setAttribute("model", orgnazitionVO);
                    }
                }

            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return "pages/orgnazition/edit.html";
    }

}
