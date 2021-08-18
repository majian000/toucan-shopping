package com.toucan.shopping.cloud.apps.seller.web.controller.user;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserTrueNameApproveService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.entity.UserTrueNameApprove;
import com.toucan.shopping.modules.user.vo.UserTrueNameApproveVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 用户实名审核
 */
@Controller("pageUserTrueNameApprovePageController")
@RequestMapping("/page/user/true/name/approve")
public class UserTrueNameApprovePageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignUserTrueNameApproveService feignUserTrueNameApproveService;

    @Autowired
    private FeignUserService feignUserService;

    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/submit_success")
    public String submit_success(HttpServletRequest request){
        return "user/trueName/true_name_submit_success";
    }

    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/update")
    public String updatePage(HttpServletRequest request)
    {
        try {
            //从请求头中拿到uid
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));

            //先查询实名状态,如果已经实名了,直接跳转认证成功页面
            UserVO userVO = new UserVO();
            userVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), userVO);
            ResultObjectVO resultObjectVO = feignUserService.verifyRealName(requestJsonVO.sign(), requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                boolean result = Boolean.valueOf(String.valueOf(resultObjectVO.getData()));
                if (result) {
                    //审核通过
                    return "user/trueName/true_name_success";
                }
            }

            UserTrueNameApproveVO queryUserTrueNameApproveVO = new UserTrueNameApproveVO();
            queryUserTrueNameApproveVO.setUserMainId(Long.parseLong(userMainId));
            requestJsonVO = RequestJsonVOGenerator.generator(getAppCode(),queryUserTrueNameApproveVO);
            //查询当前人的实名审核记录
            resultObjectVO = feignUserTrueNameApproveService.queryByUserMainId(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<UserTrueNameApprove> userTrueNameApproves = (List<UserTrueNameApprove>)resultObjectVO.formatDataArray(UserTrueNameApprove.class);
                if(CollectionUtils.isNotEmpty(userTrueNameApproves))
                {
                    UserTrueNameApprove userTrueNameApprove = userTrueNameApproves.get(0);
                    if(userTrueNameApprove.getApproveStatus().intValue()==1)
                    {
                        //审核中
                        return "user/trueName/true_name_submit_success";
                    }
                    if(userTrueNameApprove.getApproveStatus().intValue()==2)
                    {
                        //审核通过
                        return "user/trueName/true_name_success";
                    }
                    if(userTrueNameApprove.getApproveStatus().intValue()==3)
                    {
                        request.setAttribute("userTrueNameApprove",userTrueNameApprove);
                        //审核驳回
                        return "user/trueName/true_name_update";
                    }
                }
            }

        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "user/trueName/true_name_update";
    }




    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/page")
    public String page(HttpServletRequest request)
    {
        try {

            //从请求头中拿到uid
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            //先查询实名状态,如果已经实名了,直接跳转认证成功页面
            UserVO userVO = new UserVO();
            userVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), userVO);
            ResultObjectVO resultObjectVO = feignUserService.verifyRealName(requestJsonVO.sign(), requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                boolean result = Boolean.valueOf(String.valueOf(resultObjectVO.getData()));
                if (result) {
                    //审核通过
                    return "user/trueName/true_name_success";
                }
            }

            UserTrueNameApproveVO queryUserTrueNameApproveVO = new UserTrueNameApproveVO();
            queryUserTrueNameApproveVO.setUserMainId(Long.parseLong(userMainId));
            requestJsonVO = RequestJsonVOGenerator.generator(getAppCode(),queryUserTrueNameApproveVO);
            //查询当前人的实名审核记录
            resultObjectVO = feignUserTrueNameApproveService.queryByUserMainId(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<UserTrueNameApprove> userTrueNameApproves = resultObjectVO.formatDataArray(UserTrueNameApprove.class);
                if(CollectionUtils.isNotEmpty(userTrueNameApproves))
                {
                    UserTrueNameApprove userTrueNameApprove = userTrueNameApproves.get(0);
                    if(userTrueNameApprove.getApproveStatus().intValue()==1)
                    {
                        //审核中
                        return "user/trueName/true_name_submit_success";
                    }
                    if(userTrueNameApprove.getApproveStatus().intValue()==2)
                    {
                        //审核通过
                        return "user/trueName/true_name_success";
                    }
                    if(userTrueNameApprove.getApproveStatus().intValue()==3)
                    {
                        //审核驳回
                        request.setAttribute("rejectText",userTrueNameApprove.getRejectText());
                        return "user/trueName/true_name_faild";
                    }
                }
            }

        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "user/trueName/true_name";
    }


}
