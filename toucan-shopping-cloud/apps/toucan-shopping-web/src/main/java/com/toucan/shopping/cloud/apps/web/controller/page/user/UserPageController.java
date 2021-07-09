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

    @Autowired
    private FeignUserTrueNameApproveService feignUserTrueNameApproveService;


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

    @RequestMapping("/trueName")
    public String trueName(HttpServletRequest request)
    {
        try {
            //从请求头中拿到uid
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            UserTrueNameApproveVO queryUserTrueNameApproveVO = new UserTrueNameApproveVO();
            queryUserTrueNameApproveVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(getAppCode(),queryUserTrueNameApproveVO);
            //查询当前人的实名审核记录
            ResultObjectVO resultObjectVO = feignUserTrueNameApproveService.queryByUserMainId(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<UserTrueNameApprove> userTrueNameApproves = (List<UserTrueNameApprove>)resultObjectVO.formatDataArray(UserTrueNameApprove.class);
                if(CollectionUtils.isNotEmpty(userTrueNameApproves))
                {
                    request.setAttribute("userTrueNameApprove",userTrueNameApproves.get(0));
                }
            }

        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "user/true_name";
    }


}
