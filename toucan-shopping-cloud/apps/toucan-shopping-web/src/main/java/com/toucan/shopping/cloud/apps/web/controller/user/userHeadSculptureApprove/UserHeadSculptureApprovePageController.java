package com.toucan.shopping.cloud.apps.web.controller.user.userHeadSculptureApprove;

import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.service.LoginUserService;
import com.toucan.shopping.cloud.user.api.UserHeadSculptureApproveServiceAPI;
import com.toucan.shopping.cloud.user.api.UserServiceAPI;

import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.entity.UserHeadSculptureApprove;
import com.toucan.shopping.modules.user.vo.UserHeadSculptureApproveVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 用户头像审核
 */
@Controller("pageUserHeadSculptureApprovePageController")
@RequestMapping("/page/user/head/sculpture/approve")
public class UserHeadSculptureApprovePageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;
    
    @Autowired
    private UserServiceAPI userService;

    @Autowired
    private UserHeadSculptureApproveServiceAPI userHeadSculptureApproveService;

    @Autowired
    private LoginUserService loginUserService;


    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/page")
    public String page(HttpServletRequest request)
    {
        loginUserService.setAttributeUser(request);
        return "user/headSculpture/edit_head_sculpture";
    }




}
