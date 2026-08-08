package com.toucan.shopping.cloud.apps.admin.controller.user;


import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.user.api.UserTrueNameApproveServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.user.vo.UserTrueNameApproveVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户实名审核管理 - 页面控制器
 */
@Controller
public class UserTrueNameApprovePageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private UserTrueNameApproveServiceAPI userTrueNameApproveService;

    @Autowired
    private ImageUploadService imageUploadService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/userTrueNameApprove/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/userTrueNameApprove/listPage", functionServiceAPI);
        return "pages/user/trueNameApprove/list.html";
    }


    /**
     * 跳转到驳回页面
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/userTrueNameApprove/reject/page/{id}",method = RequestMethod.GET)
    public String rejectPage(HttpServletRequest request,@PathVariable String id)
    {
        UserTrueNameApproveVO userTrueNameApproveVO = new UserTrueNameApproveVO();
        try {
            userTrueNameApproveVO.setId(Long.parseLong(id));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userTrueNameApproveVO);
            ResultObjectVO resultObjectVO = userTrueNameApproveService.queryById(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<UserTrueNameApproveVO> userTrueNameApproveVOS = resultObjectVO.formatDataList(UserTrueNameApproveVO.class);
                if(CollectionUtils.isNotEmpty(userTrueNameApproveVOS)) {
                    userTrueNameApproveVO = userTrueNameApproveVOS.get(0);
                    if(userTrueNameApproveVO.getIdcardImg1()!=null) {
                        userTrueNameApproveVO.setHttpIdcardImg1(imageUploadService.getImageHttpPrefix() + userTrueNameApproveVO.getIdcardImg1());
                    }
                    if(userTrueNameApproveVO.getIdcardImg2()!=null) {
                        userTrueNameApproveVO.setHttpIdcardImg2(imageUploadService.getImageHttpPrefix() + userTrueNameApproveVO.getIdcardImg2());
                    }
                    request.setAttribute("model",userTrueNameApproveVO);
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            request.setAttribute("model", userTrueNameApproveVO);
        }
        return "pages/user/trueNameApprove/reject.html";
    }

}
