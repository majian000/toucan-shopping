package com.toucan.shopping.cloud.apps.admin.controller.user;


import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.user.api.UserHeadSculptureApproveServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.user.vo.UserHeadSculptureApproveVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户头像审核管理 - 页面控制器
 */
@Controller
public class UserHeadSculptureApprovePageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private UserHeadSculptureApproveServiceAPI userHeadSculptureApproveService;

    @Autowired
    private ImageUploadService imageUploadService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/userHeadSculptureApprove/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/userHeadSculptureApprove/listPage", functionServiceAPI);
        return "pages/user/headSculptureApprove/list.html";
    }


    /**
     * 跳转到驳回页面
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/userHeadSculptureApprove/reject/page/{id}",method = RequestMethod.GET)
    public String rejectPage(HttpServletRequest request,@PathVariable String id)
    {
        UserHeadSculptureApproveVO userHeadSculptureApproveVO = new UserHeadSculptureApproveVO();
        try {
            userHeadSculptureApproveVO.setId(Long.parseLong(id));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userHeadSculptureApproveVO);
            ResultObjectVO resultObjectVO = userHeadSculptureApproveService.queryById(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<UserHeadSculptureApproveVO> userHeadSculptureApproveVOS = resultObjectVO.formatDataList(UserHeadSculptureApproveVO.class);
                if(CollectionUtils.isNotEmpty(userHeadSculptureApproveVOS)) {
                    userHeadSculptureApproveVO = userHeadSculptureApproveVOS.get(0);

                    if(StringUtils.isNotEmpty(userHeadSculptureApproveVO.getHeadSculpture())) {
                        userHeadSculptureApproveVO.setHttpHeadSculpture(imageUploadService.getImageHttpPrefix()+"/"+userHeadSculptureApproveVO.getHeadSculpture());
                    }
                    request.setAttribute("model",userHeadSculptureApproveVO);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            request.setAttribute("model", userHeadSculptureApproveVO);
        }
        return "pages/user/headSculptureApprove/reject.html";
    }

}
