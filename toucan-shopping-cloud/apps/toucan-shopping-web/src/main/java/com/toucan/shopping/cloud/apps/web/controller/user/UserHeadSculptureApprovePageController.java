package com.toucan.shopping.cloud.apps.web.controller.user;

import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserHeadSculptureApproveService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserHeadSculptureApproveService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.entity.UserHeadSculptureApprove;
import com.toucan.shopping.modules.user.vo.UserHeadSculptureApproveVO;
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
 * 用户头像审核
 */
@Controller("pageUserHeadSculptureApprovePageController")
@RequestMapping("/page/user/head/sculpture/approve")
public class UserHeadSculptureApprovePageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;
    
    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private FeignUserHeadSculptureApproveService feignUserHeadSculptureApproveService;


    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/page")
    public String page(HttpServletRequest request)
    {
        try {
            //从请求头中拿到uid
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));


            UserHeadSculptureApproveVO queryUserHeadSculptureApproveVO = new UserHeadSculptureApproveVO();
            queryUserHeadSculptureApproveVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(getAppCode(),queryUserHeadSculptureApproveVO);
            //查询当前人的头像审核记录
            ResultObjectVO resultObjectVO = feignUserHeadSculptureApproveService.queryByUserMainId(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<UserHeadSculptureApprove> userHeadSculptureApproves = resultObjectVO.formatDataList(UserHeadSculptureApprove.class);
                if(CollectionUtils.isNotEmpty(userHeadSculptureApproves))
                {
                    UserHeadSculptureApprove userHeadSculptureApprove = userHeadSculptureApproves.get(0);
                    if(userHeadSculptureApprove.getApproveStatus().intValue()==1)
                    {
                        //审核中
                        return "user/headSculpture/submit_success";
                    }
                    if(userHeadSculptureApprove.getApproveStatus().intValue()==2)
                    {
                        //审核通过,可以接着上传图片
                        return "user/headSculpture/edit_head_sculpture";
                    }
                    if(userHeadSculptureApprove.getApproveStatus().intValue()==3)
                    {
                        //审核驳回,可以接着上传图片
                        request.setAttribute("rejectText",userHeadSculptureApprove.getRejectText());
                        return "user/headSculpture/faild";
                    }
                }
            }

        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "user/headSculpture/edit_head_sculpture";
    }






    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/reupload/page")
    public String reUploadPage(HttpServletRequest request)
    {
        try {
            //从请求头中拿到uid
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));


            UserHeadSculptureApproveVO queryUserHeadSculptureApproveVO = new UserHeadSculptureApproveVO();
            queryUserHeadSculptureApproveVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(getAppCode(),queryUserHeadSculptureApproveVO);
            //查询当前人的头像审核记录
            ResultObjectVO resultObjectVO = feignUserHeadSculptureApproveService.queryByUserMainId(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<UserHeadSculptureApprove> userHeadSculptureApproves = resultObjectVO.formatDataList(UserHeadSculptureApprove.class);
                if(CollectionUtils.isNotEmpty(userHeadSculptureApproves))
                {
                    UserHeadSculptureApprove userHeadSculptureApprove = userHeadSculptureApproves.get(0);
                    if(userHeadSculptureApprove.getApproveStatus().intValue()==1)
                    {
                        //审核中
                        return "user/headSculpture/submit_success";
                    }else{

                        //审核通过,可以接着上传图片
                        return "user/headSculpture/edit_head_sculpture";
                    }
                }
            }

        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "user/headSculpture/edit_head_sculpture";
    }


}
