package com.toucan.shopping.cloud.apps.admin.controller.message;

import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.message.api.MessageTypeServiceAPI;
import com.toucan.shopping.cloud.message.api.MessageUserServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.message.vo.MessageTypeVO;
import com.toucan.shopping.modules.message.vo.MessageUserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户消息管理 - 页面控制器
 */
@Controller
public class MessageUserPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private MessageUserServiceAPI messageUserService;

    @Autowired
    private MessageTypeServiceAPI messageTypeService;

    void initMessageTypes(HttpServletRequest request) {
        try {
            MessageTypeVO messageTypeVO = new MessageTypeVO();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), messageTypeVO);
            ResultObjectVO resultObjectVO = messageTypeService.queryList(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    request.setAttribute("messageTypes", resultObjectVO.formatDataList(MessageTypeVO.class));
                } else {
                    request.setAttribute("messageTypes", new ArrayList<>());
                }
            }
        } catch (Exception e) {
            request.setAttribute("messageTypes", new ArrayList<>());
            logger.warn(e.getMessage(), e);
        }
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/messageUser/listPage", method = RequestMethod.GET)
    public String listPage(HttpServletRequest request) {
        super.initButtons(request, toucan, "/messageUser/listPage", functionServiceAPI);
        initMessageTypes(request);
        return "pages/message/messageUser/list.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/messageUser/addPage", method = RequestMethod.GET)
    public String addPage(HttpServletRequest request) {
        initMessageTypes(request);
        return "pages/message/messageUser/add.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/messageUser/selectUserListPage", method = RequestMethod.GET)
    public String selectUserListPage(HttpServletRequest request) {
        return "pages/message/messageUser/user_list.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/messageUser/editPage/{id}", method = RequestMethod.GET)
    public String editPage(HttpServletRequest request, @PathVariable Long id) {
        try {
            MessageUserVO queryEntity = new MessageUserVO();
            queryEntity.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryEntity);
            ResultObjectVO resultObjectVO = messageUserService.findById(requestJsonVO);
            if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                if (resultObjectVO.getData() != null) {
                    List<MessageUserVO> messageTypeVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), MessageUserVO.class);
                    if (!CollectionUtils.isEmpty(messageTypeVOS)) {
                        request.setAttribute("model", messageTypeVOS.get(0));
                    }
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        initMessageTypes(request);
        return "pages/message/messageUser/edit.html";
    }

}
