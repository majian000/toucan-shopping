package com.toucan.shopping.cloud.apps.seller.web.controller.designer.image;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerDesignerImageService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.shop.ShopAuth;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.SellerDesignerImageVO;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 店铺装修图片
 */
@Controller("sellerDesignerImagePageController")
@RequestMapping("/page/designer/image")
public class SellerDesignerImagePageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignSellerDesignerImageService feignSellerDesignerImageService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private ImageUploadService imageUploadService;


    @ShopAuth
    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/list")
    public String list(HttpServletRequest request)
    {
        return "designer/image/index";
    }



    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/add")
    public String add(HttpServletRequest request){

        return "designer/image/add";
    }



    private SellerShopVO queryByShop(String userMainId) throws Exception
    {
        SellerShop querySellerShop = new SellerShop();
        querySellerShop.setUserMainId(Long.parseLong(userMainId));
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
        ResultObjectVO resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
        if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null) {
            SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
            return sellerShopVO;
        }
        return null;
    }


    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/edit/{id}")
    public String edit(HttpServletRequest request,@PathVariable Long id){

        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            SellerShopVO sellerShopVO = queryByShop(userMainId);
            if(sellerShopVO!=null)
            {
                SellerDesignerImageVO sellerDesignerImageVO = new SellerDesignerImageVO();
                sellerDesignerImageVO.setId(id);
                ResultObjectVO resultObjectVO = feignSellerDesignerImageService.findById(RequestJsonVOGenerator.generator(toucan.getAppCode(),sellerDesignerImageVO));
                if(resultObjectVO.isSuccess())
                {
                    SellerDesignerImageVO resultDesignerImageVO = resultObjectVO.formatData(SellerDesignerImageVO.class);
                    if(resultDesignerImageVO!=null&&resultDesignerImageVO.getShopId().equals(sellerShopVO.getId()))
                    {
                        if(resultDesignerImageVO.getImgPath()!=null) {
                            resultDesignerImageVO.setHttpImgPath(imageUploadService.getImageHttpPrefix()+resultDesignerImageVO.getImgPath());
                        }

                        request.setAttribute("designerImage",resultDesignerImageVO);
                    }
                }
            }
        }catch(Exception e)
        {
            logger.error(e.getMessage(),e);
        }

        return "designer/image/edit";
    }

}
