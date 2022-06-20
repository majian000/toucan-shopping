package com.toucan.shopping.cloud.apps.message.web.controller;

import com.toucan.shopping.cloud.message.api.feign.service.FeignMessageUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.message.constant.AppCodeConstant;
import com.toucan.shopping.modules.message.page.MessageUserPageInfo;
import com.toucan.shopping.modules.message.vo.MessageUserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller("messageController")
@RequestMapping("/api/user/message")
public class UserMessageController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignMessageUserService feignMessageUserService;

    @UserAuth
    @RequestMapping("/list")
    @ResponseBody
    public ResultObjectVO queryMyMessageList(MessageUserPageInfo messageUserPageInfo,HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId = "-1";
        try {
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            messageUserPageInfo.setUserMainId(Long.parseLong(userMainId));
            if(messageUserPageInfo.getSrcType()!=null&&messageUserPageInfo.getSrcType().intValue()==1)
            {
                messageUserPageInfo.setMessageTypeAppCode(AppCodeConstant.SHOPPING_WEB);
            }else if(messageUserPageInfo.getSrcType()!=null&&messageUserPageInfo.getSrcType().intValue()==2)
            {
                messageUserPageInfo.setMessageTypeAppCode(AppCodeConstant.SELLER_WEB);
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),messageUserPageInfo);
            resultObjectVO = feignMessageUserService.queryListPageByUserMianId(requestJsonVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    @UserAuth
    @RequestMapping("/unread/count")
    @ResponseBody
    public ResultObjectVO queryUnreadCount(HttpServletRequest request, @RequestParam Integer srcType)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId = "-1";
        try {
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            MessageUserVO messageUserVO=new MessageUserVO();
            messageUserVO.setUserMainId(Long.parseLong(userMainId));
            if(srcType!=null&&srcType.intValue()==1)
            {
                messageUserVO.setMessageTypeAppCode(AppCodeConstant.SHOPPING_WEB);
            }else if(srcType!=null&&srcType.intValue()==2)
            {
                messageUserVO.setMessageTypeAppCode(AppCodeConstant.SELLER_WEB);
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),messageUserVO);

            resultObjectVO = feignMessageUserService.queryUnreadCountByUserMainId(requestJsonVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    @UserAuth
    @RequestMapping("/read/{id}")
    @ResponseBody
    public ResultObjectVO readByIdAndUserMainId(HttpServletRequest request, @PathVariable String id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId = "-1";
        try {
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            MessageUserVO messageUserVO=new MessageUserVO();
            messageUserVO.setUserMainId(Long.parseLong(userMainId));
            messageUserVO.setId(Long.parseLong(id));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),messageUserVO);

            resultObjectVO = feignMessageUserService.updateReadStatus(requestJsonVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




    @UserAuth
    @RequestMapping("/read/all")
    @ResponseBody
    public ResultObjectVO readAll(HttpServletRequest request, @RequestParam Integer srcType)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId = "-1";
        try {
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            MessageUserVO messageUserVO=new MessageUserVO();
            messageUserVO.setUserMainId(Long.parseLong(userMainId));
            if(srcType!=null&&srcType.intValue()==1)
            {
                messageUserVO.setMessageTypeAppCode(AppCodeConstant.SHOPPING_WEB);
            }else if(srcType!=null&&srcType.intValue()==2)
            {
                messageUserVO.setMessageTypeAppCode(AppCodeConstant.SELLER_WEB);
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),messageUserVO);

            resultObjectVO = feignMessageUserService.updateAllReadStatus(requestJsonVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

}
