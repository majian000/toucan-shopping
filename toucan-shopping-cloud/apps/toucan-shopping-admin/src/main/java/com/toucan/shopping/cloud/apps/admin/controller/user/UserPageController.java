package com.toucan.shopping.cloud.apps.admin.controller.user;


import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.cloud.user.api.UserServiceAPI;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.constant.TableButtons;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private UserServiceAPI userService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/user/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/user/listPage", functionServiceAPI);

        this.initRowMoreButtons(request, TableButtons.TABLE_MORE_BUTTON);

        return "pages/user/db/list.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/user/registPage",method = RequestMethod.GET)
    public String registPage()
    {
        return "pages/user/db/regist.html";
    }


    /**
     * 手机号列表页
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/user/mobilePhoneListPage/{userMainId}",method = RequestMethod.GET)
    public String mobilePhoneListPage(HttpServletRequest request,@PathVariable String userMainId)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/user/mobilePhoneListPage", functionServiceAPI);
        request.setAttribute("userMainId",userMainId);
        return "pages/user/db/mobile_phone_list.html";
    }

    /**
     * 邮箱列表
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/user/emailListPage/{userMainId}",method = RequestMethod.GET)
    public String emailListPage(HttpServletRequest request,@PathVariable String userMainId)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/user/emailListPage", functionServiceAPI);
        request.setAttribute("userMainId",userMainId);
        return "pages/user/db/email_list.html";
    }

    /**
     * 用户名列表
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/user/userNameListPage/{userMainId}",method = RequestMethod.GET)
    public String userNameListPage(HttpServletRequest request,@PathVariable String userMainId)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/user/userNameListPage", functionServiceAPI);
        request.setAttribute("userMainId",userMainId);
        return "pages/user/db/username_list.html";
    }

    /**
     * 关联手机号页
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/user/connectMobilePhonePage/{userMainId}",method = RequestMethod.GET)
    public String connectMobilePhonePage(HttpServletRequest request,@PathVariable String userMainId)
    {
        request.setAttribute("userMainId",userMainId);
        return "pages/user/db/connect_mobile_phone.html";
    }


    /**
     * 关联邮箱页
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/user/connectEmailPage/{userMainId}",method = RequestMethod.GET)
    public String connectEmailPage(HttpServletRequest request,@PathVariable String userMainId)
    {
        request.setAttribute("userMainId",userMainId);
        return "pages/user/db/connect_email.html";
    }


    /**
     * 跳转到修改用户资料页
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/user/update/detail/page/{userMainId}",method = RequestMethod.GET)
    public String updateDetailPage(HttpServletRequest request,@PathVariable String userMainId)
    {
        UserVO userVO = new UserVO();
        try {
            userVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userVO);
            ResultObjectVO resultObjectVO = userService.findByUserMainId(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                userVO =   resultObjectVO.formatData(UserVO.class);
                if(userVO.getHeadSculpture()!=null) {
                    userVO.setHttpHeadSculpture(imageUploadService.getImageHttpPrefix() + userVO.getHeadSculpture());
                }
                if(userVO.getIdcardImg1()!=null)
                {
                    userVO.setHttpIdcardImg1(imageUploadService.getImageHttpPrefix() + userVO.getIdcardImg1());
                }
                if(userVO.getIdcardImg2()!=null)
                {
                    userVO.setHttpIdcardImg2(imageUploadService.getImageHttpPrefix() + userVO.getIdcardImg2());
                }
                request.setAttribute("model", userVO);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            request.setAttribute("model", userVO);
        }
        return "pages/user/db/update_detail.html";
    }


    /**
     * 查看详情页
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/user/detail/page/{userMainId}",method = RequestMethod.GET)
    public String detailPage(HttpServletRequest request,@PathVariable String userMainId)
    {
        UserVO userVO = new UserVO();
        try {
            userVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userVO);
            ResultObjectVO resultObjectVO = userService.findByUserMainId(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                userVO =  resultObjectVO.formatData(UserVO.class);
                if(userVO.getHeadSculpture()!=null) {
                    userVO.setHttpHeadSculpture(imageUploadService.getImageHttpPrefix() + userVO.getHeadSculpture());
                }

                if(userVO.getIdcardImg1()!=null)
                {
                    userVO.setHttpIdcardImg1(imageUploadService.getImageHttpPrefix() + userVO.getIdcardImg1());
                }
                if(userVO.getIdcardImg2()!=null)
                {
                    userVO.setHttpIdcardImg2(imageUploadService.getImageHttpPrefix() + userVO.getIdcardImg2());
                }
                request.setAttribute("model", userVO);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            request.setAttribute("model", userVO);
        }
        return "pages/user/db/detail.html";
    }


    /**
     * 关联用户名页
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/user/connectUserNamePage/{userMainId}",method = RequestMethod.GET)
    public String connectUserNamePage(HttpServletRequest request,@PathVariable String userMainId)
    {
        request.setAttribute("userMainId",userMainId);
        return "pages/user/db/connect_username.html";
    }


    /**
     * 重置密码页
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/user/resetPasswordPage/{userMainId}",method = RequestMethod.GET)
    public String resetPasswordPage(HttpServletRequest request,@PathVariable String userMainId)
    {
        request.setAttribute("userMainId",userMainId);
        return "pages/user/db/reset_password.html";
    }

}
