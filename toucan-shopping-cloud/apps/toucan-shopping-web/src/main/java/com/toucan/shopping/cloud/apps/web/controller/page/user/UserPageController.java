package com.toucan.shopping.cloud.apps.web.controller.page.user;

import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserTrueNameApproveService;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.entity.UserTrueNameApprove;
import com.toucan.shopping.modules.user.vo.UserTrueNameApproveVO;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 用户控制器
 */
@Controller("pageUserController")
@RequestMapping("/page/user")
public class UserPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @RequestMapping("/regist")
    public String regist()
    {
        return "user/regist";
    }

    @RequestMapping("/login")
    public String login()
    {
        return "user/login";
    }


    @RequestMapping("/info")
    public String center()
    {
        return "user/info";
    }



}
