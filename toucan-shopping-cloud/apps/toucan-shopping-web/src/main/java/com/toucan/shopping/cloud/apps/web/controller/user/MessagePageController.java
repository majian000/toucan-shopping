package com.toucan.shopping.cloud.apps.web.controller.user;

import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserHeadSculptureApproveService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.properties.Toucan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * 用户头像审核
 */
@Controller("pageMessageCenterPageController")
@RequestMapping("/page/user/message")
public class MessagePageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());



    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/")
    public String page(HttpServletRequest request)
    {
        return "user/message/message_list";
    }




}
