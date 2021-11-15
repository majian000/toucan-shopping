package com.toucan.shopping.cloud.apps.seller.web.controller.shop;

import com.alibaba.fastjson.JSONArray;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.area.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 店铺信息
 */
@Controller("shopApiController")
@RequestMapping("/api/shop")
public class ShopApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private ImageUploadService imageUploadService;




    @RequestMapping(value="/shop/info")
    @ResponseBody
    public ResultObjectVO loginInfo(HttpServletRequest httpServletRequest){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            UserVO queryUserVO = new UserVO();
            queryUserVO.setUserMainId(Long.parseLong(UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()))));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryUserVO);
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);

                if(sellerShopVO.getLogo()!=null) {
                    sellerShopVO.setHttpLogo(imageUploadService.getImageHttpPrefix() + "/" + sellerShopVO.getLogo());
                }else{
                    //设置默认店铺图标
                    if(toucan.getSeller()!=null&&toucan.getSeller().getDefaultShopLogo()!=null) {
                        sellerShopVO.setHttpLogo(imageUploadService.getImageHttpPrefix() + "/" + toucan.getSeller().getDefaultShopLogo());
                    }
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }





}
