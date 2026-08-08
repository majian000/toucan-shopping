package com.toucan.shopping.cloud.apps.admin.controller.seller;


import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.seller.api.ShopBannerServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.seller.vo.ShopBannerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 卖家轮播图 - 页面控制器
 */
@Controller
public class ShopBannerPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private ShopBannerServiceAPI shopBannerService;

    @Autowired
    private ImageUploadService imageUploadService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/shopBanner/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/shopBanner/listPage", functionServiceAPI);
        return "pages/seller/shopBanner/list.html";
    }


    /**
     * 编辑页
     * @param request
     * @param id
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/shopBanner/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            ShopBannerVO banner = new ShopBannerVO();
            banner.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, banner);
            ResultObjectVO resultObjectVO = shopBannerService.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    banner = resultObjectVO.formatData(ShopBannerVO.class);
                    if(banner!=null)
                    {
                        banner.setHttpImgPath(imageUploadService.getImageHttpPrefix() + banner.getImgPath());
                        if(banner.getStartShowDate()!=null) {
                            banner.setStartShowDateString(DateUtils.format(banner.getStartShowDate(), DateUtils.FORMATTER_SS.get()));
                        }

                        if(banner.getEndShowDate()!=null) {
                            banner.setEndShowDateString(DateUtils.format(banner.getEndShowDate(), DateUtils.FORMATTER_SS.get()));
                        }

                        request.setAttribute("model",banner);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/seller/shopBanner/edit.html";
    }


    /**
     * 查看详情页
     * @param request
     * @param id
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/shopBanner/detailPage/{id}",method = RequestMethod.GET)
    public String detailPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            ShopBannerVO banner = new ShopBannerVO();
            banner.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, banner);
            ResultObjectVO resultObjectVO = shopBannerService.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    banner = resultObjectVO.formatData(ShopBannerVO.class);
                    if(banner!=null)
                    {
                        banner.setHttpImgPath(imageUploadService.getImageHttpPrefix() + banner.getImgPath());
                        if(banner.getStartShowDate()!=null) {
                            banner.setStartShowDateString(DateUtils.format(banner.getStartShowDate(), DateUtils.FORMATTER_SS.get()));
                        }

                        if(banner.getEndShowDate()!=null) {
                            banner.setEndShowDateString(DateUtils.format(banner.getEndShowDate(), DateUtils.FORMATTER_SS.get()));
                        }

                        request.setAttribute("model",banner);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/seller/shopBanner/detail.html";
    }

}
