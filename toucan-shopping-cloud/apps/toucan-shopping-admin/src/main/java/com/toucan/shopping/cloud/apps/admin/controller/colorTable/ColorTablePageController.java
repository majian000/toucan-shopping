package com.toucan.shopping.cloud.apps.admin.controller.colorTable;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.ColorTableServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.color.table.vo.ColorTableVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ColorTablePageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private ColorTableServiceAPI colorTableService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/colorTable/listPage", method = RequestMethod.GET)
    public String listPage(HttpServletRequest request) {
        super.initButtons(request, toucan, "/colorTable/listPage", functionServiceAPI);
        return "pages/colorTable/list.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/colorTable/addPage", method = RequestMethod.GET)
    public String addPage(HttpServletRequest request) {
        return "pages/colorTable/add.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/colorTable/editPage/{id}", method = RequestMethod.GET)
    public String editPage(HttpServletRequest request, @PathVariable Long id) {
        try {
            ColorTableVO colorTableVO = new ColorTableVO();
            colorTableVO.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, colorTableVO);
            ResultObjectVO resultObjectVO = colorTableService.findById(requestJsonVO);
            if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                if (resultObjectVO.getData() != null) {
                    List<ColorTableVO> colorTableVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), ColorTableVO.class);
                    if (!CollectionUtils.isEmpty(colorTableVOS)) {
                        colorTableVO = colorTableVOS.get(0);
                        request.setAttribute("model", colorTableVO);
                    }
                }

            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return "pages/colorTable/edit.html";
    }

}
