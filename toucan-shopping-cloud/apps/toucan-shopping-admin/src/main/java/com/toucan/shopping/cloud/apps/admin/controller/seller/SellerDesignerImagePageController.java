package com.toucan.shopping.cloud.apps.admin.controller.seller;


import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.seller.api.SellerDesignerImageServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.seller.vo.SellerDesignerImageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 装修图片 - 页面控制器
 */
@Controller
public class SellerDesignerImagePageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private SellerDesignerImageServiceAPI sellerDesignerImageService;

    @Autowired
    private ImageUploadService imageUploadService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/sellerDesignerImage/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/sellerDesignerImage/listPage", functionServiceAPI);
        return "pages/seller/designer/image/list.html";
    }


    /**
     * 编辑页
     * @param request
     * @param id
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/sellerDesignerImage/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            SellerDesignerImageVO sellerDesignerImageVO = new SellerDesignerImageVO();
            sellerDesignerImageVO.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, sellerDesignerImageVO);
            ResultObjectVO resultObjectVO = sellerDesignerImageService.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    sellerDesignerImageVO = resultObjectVO.formatData(SellerDesignerImageVO.class);
                    if(sellerDesignerImageVO!=null)
                    {
                        sellerDesignerImageVO.setHttpImgPath(imageUploadService.getImageHttpPrefix() + sellerDesignerImageVO.getImgPath());
                        request.setAttribute("model",sellerDesignerImageVO);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/seller/designer/image/edit.html";
    }


    /**
     * 查看详情页
     * @param request
     * @param id
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/sellerDesignerImage/detailPage/{id}",method = RequestMethod.GET)
    public String detailPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            SellerDesignerImageVO sellerDesignerImageVO = new SellerDesignerImageVO();
            sellerDesignerImageVO.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, sellerDesignerImageVO);
            ResultObjectVO resultObjectVO = sellerDesignerImageService.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    sellerDesignerImageVO = resultObjectVO.formatData(SellerDesignerImageVO.class);
                    if(sellerDesignerImageVO!=null)
                    {
                        sellerDesignerImageVO.setHttpImgPath(imageUploadService.getImageHttpPrefix() + sellerDesignerImageVO.getImgPath());
                        request.setAttribute("model",sellerDesignerImageVO);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/seller/designer/image/detail.html";
    }

}
