package com.toucan.shopping.cloud.apps.seller.web.controller.index;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.area.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 首页控制器
 */
@Controller("pageIndexController")
public class IndexPageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignAreaService feignAreaService;

    @Autowired
    private ImageUploadService imageUploadService;



    @RequestMapping("/freeShop")
    public String freeShop(HttpServletRequest request)
    {
        return "/htmls/release/freeShop";
    }


    public String shopInfo(HttpServletRequest httpServletRequest)
    {

        try {
            UserVO userVO = new UserVO();
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            userVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userVO);
            ResultObjectVO resultObjectVO = feignUserService.verifyRealName(requestJsonVO.sign(), requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                boolean result = Boolean.valueOf(String.valueOf(resultObjectVO.getData()));
                if(result)
                {
                    SellerShop querySellerShop = new SellerShop();
                    querySellerShop.setUserMainId(userVO.getUserMainId());
                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), querySellerShop);
                    //判断是个人店铺还是企业店铺
                    resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
                    if(resultObjectVO.isSuccess())
                    {
                        //该账号存在店铺
                        SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                        if(sellerShopVO==null)
                        {
                            return "shop/select_regist_type";
                        }

                        //设置店铺logo
                        if(sellerShopVO.getLogo()!=null) {
                            sellerShopVO.setHttpLogo(imageUploadService.getImageHttpPrefix() + "/" + sellerShopVO.getLogo());
                        }
                        //个人店铺
                        if(sellerShopVO.getType().intValue()==1)
                        {
                            httpServletRequest.setAttribute("sellerShop",sellerShopVO);
                            return "shop/info";
                        }

                    }

                }else{
                    return "shop/please_true_name";
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "/htmls/release/freeShop";
    }

    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/")
    public String defaultIndex(HttpServletRequest request)
    {
        return shopInfo(request);
    }


    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/index")
    public String index(HttpServletRequest request)
    {
        return shopInfo(request);
    }

}
