package com.toucan.shopping.cloud.apps.web.controller.user.buyCar;


import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户购物车
 */
@Controller("buyCarApiController")
@RequestMapping("/api/user/buyCar")
public class UserBuyApiPageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());



    @UserAuth
    @RequestMapping("/preview/info")
    @ResponseBody
    public ResultObjectVO previewInfo()
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

}
