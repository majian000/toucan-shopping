package com.toucan.shopping.cloud.apps.web.controller.user.buyCar;


import com.toucan.shopping.cloud.user.api.feign.service.FeignUserBuyCarService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.vo.UserBuyCarVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户购物车
 */
@Controller("buyCarApiController")
@RequestMapping("/api/user/buyCar")
public class UserBuyApiPageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignUserBuyCarService feignUserBuyCarService;

    @Autowired
    private Toucan toucan;

    @UserAuth
    @RequestMapping("/preview/info")
    @ResponseBody
    public ResultObjectVO previewInfo(HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId="-1";
        try{
            //从请求头中拿到uid
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            UserBuyCarVO userBuyCarVO = new UserBuyCarVO();
            userBuyCarVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),userBuyCarVO);
            resultObjectVO  = feignUserBuyCarService.listByUserMainId(requestJsonVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

}
