package com.toucan.shopping.cloud.apps.seller.web.controller.user;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 店铺控制器
 */
@Controller("pageShopController")
@RequestMapping("/page/shop")
public class ShopPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignUserService feignUserService;


    @RequestMapping("/shop_regist/{uid}")
    public String center(@PathVariable Long uid)
    {

        try {
            UserVO userVO = new UserVO();
            userVO.setUserMainId(uid);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), userVO);
            ResultObjectVO resultObjectVO = feignUserService.verifyRealName(requestJsonVO.sign(), requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                boolean result = Boolean.valueOf(String.valueOf(resultObjectVO.getData()));
                if(result)
                {
                    return "shop_regist";
                }else{
                    //TODO:跳转到实名页面
                    //TODO:个人开店只需要实名就可以 企业开店 需要上传公司资质相关
                    int a=0;
                }
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "index";
    }

}
