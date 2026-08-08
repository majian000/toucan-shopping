package com.toucan.shopping.cloud.apps.admin.controller.message;

import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.message.api.MessageTypeServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.message.vo.MessageTypeVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 消息类型管理 - 页面控制器
 */
@Controller
public class MessageTypePageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private MessageTypeServiceAPI messageTypeService;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/messageType/listPage", method = RequestMethod.GET)
    public String listPage(HttpServletRequest request) {
        super.initButtons(request, toucan, "/messageType/listPage", functionServiceAPI);
        return "pages/message/messageType/list.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/messageType/addPage", method = RequestMethod.GET)
    public String addPage(HttpServletRequest request) {
        return "pages/message/messageType/add.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/messageType/editPage/{id}", method = RequestMethod.GET)
    public String editPage(HttpServletRequest request, @PathVariable Long id) {
        try {
            MessageTypeVO queryEntity = new MessageTypeVO();
            queryEntity.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryEntity);
            ResultObjectVO resultObjectVO = messageTypeService.findById(requestJsonVO);
            if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                if (resultObjectVO.getData() != null) {
                    List<MessageTypeVO> messageTypeVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), MessageTypeVO.class);
                    if (!CollectionUtils.isEmpty(messageTypeVOS)) {
                        request.setAttribute("model", messageTypeVOS.get(0));
                    }
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return "pages/message/messageType/edit.html";
    }

}
