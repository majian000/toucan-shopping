package com.toucan.shopping.cloud.apps.web.controller.user;

import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.redis.UserLoginRedisKey;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.IPUtil;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * 用户控制器
 */
@Controller("pageUserController")
@RequestMapping("/page/user")
public class UserPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Autowired
    private FeignUserService feignUserService;



    @RequestMapping("/regist")
    public String regist()
    {
        return "user/regist";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request)
    {
        //查询登录次数,失败3次要求输入验证码
        String loginFaildCountKey = UserLoginRedisKey.getLoginFaildCountKey(IPUtil.getRemoteAddr(request));
        Object loginFaildCountValueObject = toucanStringRedisService.get(loginFaildCountKey);
        if(loginFaildCountValueObject!=null) {
            Integer faildCount = Integer.parseInt(String.valueOf(loginFaildCountValueObject));
            if(faildCount>=3)
            {
                request.setAttribute("isShowInputVcode",true);
            }else{
                request.setAttribute("isShowInputVcode",false);
            }
        }else{
            request.setAttribute("isShowInputVcode",false);
        }
        return "user/login";
    }


    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/info")
    public String info(HttpServletRequest request)
    {
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(toucan.getAppCode(), request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            UserVO userVO = new UserVO();
            userVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),userVO);
            ResultObjectVO resultObectVO = feignUserService.findByUserMainIdForCacheOrDB(requestJsonVO.sign(),requestJsonVO);
            if(resultObectVO.isSuccess())
            {
                userVO = (UserVO)resultObectVO.formatData(UserVO.class);
                request.setAttribute("user",userVO);
            }else{
                request.setAttribute("user",new UserVO());
            }
        }catch (Exception e)
        {
            request.setAttribute("user",new UserVO());
            logger.warn(e.getMessage(),e);
        }
        return "user/info";
    }



}
