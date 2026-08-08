package com.toucan.shopping.cloud.apps.admin.controller.column;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.content.api.ColumnTypeServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.column.vo.ColumnTypeVO;
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
public class ColumnTypePageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private ColumnTypeServiceAPI columnTypeService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/columnType/listPage", method = RequestMethod.GET)
    public String listPage(HttpServletRequest request) {
        super.initButtons(request, toucan, "/columnType/listPage", functionServiceAPI);
        return "pages/column/columnType/list.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/columnType/addPage", method = RequestMethod.GET)
    public String addPage(HttpServletRequest request) {
        return "pages/column/columnType/add.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/columnType/editPage/{id}", method = RequestMethod.GET)
    public String editPage(HttpServletRequest request, @PathVariable Long id) {
        try {
            ColumnTypeVO queryEntity = new ColumnTypeVO();
            queryEntity.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryEntity);
            ResultObjectVO resultObjectVO = columnTypeService.findById(requestJsonVO);
            if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                if (resultObjectVO.getData() != null) {
                    List<ColumnTypeVO> columnTypeVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), ColumnTypeVO.class);
                    if (!CollectionUtils.isEmpty(columnTypeVOS)) {
                        request.setAttribute("model", columnTypeVOS.get(0));
                    }
                }

            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return "pages/column/columnType/edit.html";
    }

}
