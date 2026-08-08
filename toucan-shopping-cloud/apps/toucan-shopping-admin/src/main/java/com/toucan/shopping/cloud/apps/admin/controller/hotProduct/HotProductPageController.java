package com.toucan.shopping.cloud.apps.admin.controller.hotProduct;


import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.content.api.HotProductServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.column.vo.HotProductVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HotProductPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private HotProductServiceAPI hotProductService;

    @Autowired
    private ImageUploadService imageUploadService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/hotProduct/listPage", method = RequestMethod.GET)
    public String listPage(HttpServletRequest request) {
        super.initButtons(request, toucan, "/hotProduct/listPage", functionServiceAPI);
        return "pages/hotProduct/list.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/hotProduct/addPage", method = RequestMethod.GET)
    public String addPage(HttpServletRequest request) {
        return "pages/hotProduct/add.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/hotProduct/editPage/{id}", method = RequestMethod.GET)
    public String editPage(HttpServletRequest request, @PathVariable Long id) {
        try {
            HotProductVO hotProductVO = new HotProductVO();
            hotProductVO.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, hotProductVO);
            ResultObjectVO resultObjectVO = hotProductService.findById(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                hotProductVO = resultObjectVO.formatData(HotProductVO.class);
                if (hotProductVO != null) {
                    hotProductVO.setHttpImgPath(imageUploadService.getImageHttpPrefix() + hotProductVO.getImgPath());
                }
                request.setAttribute("model", hotProductVO);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return "pages/hotProduct/edit.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/hotProduct/showPage/{id}", method = RequestMethod.GET)
    public String showPage(HttpServletRequest request, @PathVariable Long id) {
        try {
            HotProductVO hotProductVO = new HotProductVO();
            hotProductVO.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, hotProductVO);
            ResultObjectVO resultObjectVO = hotProductService.findById(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                hotProductVO = resultObjectVO.formatData(HotProductVO.class);
                if (hotProductVO != null) {
                    hotProductVO.setHttpImgPath(imageUploadService.getImageHttpPrefix() + hotProductVO.getImgPath());
                }
                request.setAttribute("model", hotProductVO);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return "pages/hotProduct/show.html";
    }

}
