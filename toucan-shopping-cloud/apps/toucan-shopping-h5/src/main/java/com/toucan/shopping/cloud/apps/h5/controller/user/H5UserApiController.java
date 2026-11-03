package com.toucan.shopping.cloud.apps.h5.controller.user;

import com.toucan.shopping.cloud.apps.h5.controller.BaseController;
import com.toucan.shopping.cloud.user.api.UserServiceAPI;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.util.IPUtil;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.user.constant.UserLoginConstant;
import com.toucan.shopping.modules.user.vo.UserLoginVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * H5 用户登录 / 退出 / 信息
 *
 * 会话与 PC web 独立：登录种 h5_uid / h5_lt / h5_lat 三个 cookie（PC 是 tss_*），
 * 鉴权层只认 h5_*，两边互不串号。
 */
@RestController("h5UserController")
@RequestMapping("/h5/user")
public class H5UserApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserServiceAPI userService;

    @Autowired
    private ImageUploadService imageUploadService;

    /**
     * 登录（手机号或邮箱 + 密码）
     */
    @RequestMapping(value = "/login/password", produces = "application/json;charset=UTF-8")
    public ResultObjectVO loginByPassword(@RequestBody UserLoginVO userLoginVO,
                                          HttpServletResponse response,
                                          HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (userLoginVO == null || StringUtils.isEmpty(userLoginVO.getLoginUserName())) {
                resultObjectVO.setCode(UserLoginConstant.USERNAME_NOT_FOUND);
                resultObjectVO.setMsg("请输入手机号或邮箱");
                return resultObjectVO;
            }
            if (StringUtils.isEmpty(userLoginVO.getPassword())) {
                resultObjectVO.setCode(UserLoginConstant.PASSWORD_NOT_FOUND);
                resultObjectVO.setMsg("请输入密码");
                return resultObjectVO;
            }

            userLoginVO.setAppCode(this.getAppCode());
            try {
                userLoginVO.setLoginIp(IPUtil.getRemoteAddr(request));
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
            userLoginVO.setSrcType(3); // H5端登录（1=PC商城 2=卖家 3=H5）

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), userLoginVO);
            resultObjectVO = userService.loginByPassword(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                userLoginVO = resultObjectVO.formatData(UserLoginVO.class);
                if (userLoginVO == null) {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("登录失败,请重新输入");
                } else {
                    // H5 独立会话 cookie
                    addCookie(response, "h5_uid", String.valueOf(userLoginVO.getUserMainId()), UserLoginConstant.LOGIN_UID_MAX_AGE);
                    addCookie(response, "h5_lt", userLoginVO.getLoginToken(), UserLoginConstant.LOGIN_TOKEN_MAX_AGE);
                    addCookie(response, "h5_lat", userLoginVO.getLoginAuthToken(), UserLoginConstant.LOGIN_SUPER_TOKEN_MAX_AGE);
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("登录失败,请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 退出登录：清除 H5 独立会话 cookie
     */
    @RequestMapping(value = "/logout", produces = "application/json;charset=UTF-8")
    public ResultObjectVO logout(HttpServletResponse response) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            removeCookie(response, "h5_uid");
            removeCookie(response, "h5_lt");
            removeCookie(response, "h5_lat");
            resultObjectVO.setCode(ResultObjectVO.SUCCESS);
            resultObjectVO.setMsg("退出成功");
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("退出失败");
        }
        return resultObjectVO;
    }

    /**
     * 登录信息（登录后首页/用户中心顶部展示用）
     */
    @RequestMapping(value = "/login/info", produces = "application/json;charset=UTF-8")
    public ResultObjectVO loginInfo(HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            UserVO queryUserVO = new UserVO();
            queryUserVO.setUserMainId(Long.parseLong(
                    UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()))));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), queryUserVO);
            resultObjectVO = userService.queryLoginInfo(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                UserVO userVO = resultObjectVO.formatData(UserVO.class);
                if (userVO != null && StringUtils.isNotEmpty(userVO.getHeadSculpture())) {
                    userVO.setHttpHeadSculpture(imageUploadService.getImageHttpPrefix() + userVO.getHeadSculpture());
                } else if (userVO != null) {
                    userVO.setHttpHeadSculpture(imageUploadService.getImageHttpPrefix() + this.getToucan().getUser().getDefaultHeadSculpture());
                }
                resultObjectVO.setData(userVO);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 用户信息（需登录）
     */
    @UserAuth
    @RequestMapping(value = "/info", produces = "application/json;charset=UTF-8")
    public ResultObjectVO info(HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            UserVO userVO = new UserVO();
            userVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), userVO);
            ResultObjectVO resultObjectVO2 = userService.findByUserMainIdForCacheOrDB(requestJsonVO);
            if (!resultObjectVO2.isSuccess()) {
                resultObjectVO.setData(new UserVO());
            } else {
                resultObjectVO = resultObjectVO2;
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        if (value == null) {
            value = "";
        }
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    private void removeCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
