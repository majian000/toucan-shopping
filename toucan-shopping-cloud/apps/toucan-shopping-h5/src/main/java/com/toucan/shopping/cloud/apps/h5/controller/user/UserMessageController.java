package com.toucan.shopping.cloud.apps.h5.controller.user;

import com.toucan.shopping.cloud.apps.h5.controller.BaseController;
import com.toucan.shopping.cloud.message.api.MessageUserServiceAPI;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.message.constant.AppCodeConstant;
import com.toucan.shopping.modules.message.page.MessageUserPageInfo;
import com.toucan.shopping.modules.message.vo.MessageUserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

/**
 * H5 用户消息
 */
@RestController("h5UserMessageController")
@RequestMapping("/h5/user/message")
public class UserMessageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageUserServiceAPI messageUserServiceAPI;

    @UserAuth
    @RequestMapping("/list")
    public ResultObjectVO list(MessageUserPageInfo messageUserPageInfo, HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            messageUserPageInfo.setUserMainId(Long.parseLong(userMainId));
            // H5 属于商城端，消息按 SHOPPING_WEB 归属
            messageUserPageInfo.setMessageTypeAppCode(AppCodeConstant.SHOPPING_WEB);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), messageUserPageInfo);
            resultObjectVO = messageUserServiceAPI.queryListPageByUserMianId(requestJsonVO);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    @UserAuth
    @RequestMapping("/unread/count")
    public ResultObjectVO unreadCount(HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            MessageUserVO messageUserVO = new MessageUserVO();
            messageUserVO.setUserMainId(Long.parseLong(userMainId));
            messageUserVO.setMessageTypeAppCode(AppCodeConstant.SHOPPING_WEB);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), messageUserVO);
            resultObjectVO = messageUserServiceAPI.queryUnreadCountByUserMainId(requestJsonVO);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    @UserAuth
    @RequestMapping("/read/{id}")
    public ResultObjectVO read(HttpServletRequest request, @PathVariable String id) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            MessageUserVO messageUserVO = new MessageUserVO();
            messageUserVO.setUserMainId(Long.parseLong(userMainId));
            messageUserVO.setId(Long.parseLong(id));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), messageUserVO);
            resultObjectVO = messageUserServiceAPI.updateReadStatus(requestJsonVO);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    @UserAuth
    @RequestMapping("/read/all")
    public ResultObjectVO readAll(HttpServletRequest request, @RequestParam(required = false) Integer srcType) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            MessageUserVO messageUserVO = new MessageUserVO();
            messageUserVO.setUserMainId(Long.parseLong(userMainId));
            messageUserVO.setMessageTypeAppCode(AppCodeConstant.SHOPPING_WEB);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), messageUserVO);
            resultObjectVO = messageUserServiceAPI.updateAllReadStatus(requestJsonVO);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }
}
