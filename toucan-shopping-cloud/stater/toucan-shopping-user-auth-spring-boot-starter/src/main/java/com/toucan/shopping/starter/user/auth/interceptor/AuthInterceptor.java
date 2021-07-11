package com.toucan.shopping.starter.user.auth.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.spring.context.SpringContextHolder;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.common.wrapper.RequestWrapper;
import com.toucan.shopping.modules.user.vo.UserLoginVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

/**
 * 权限校验
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private SpringContextHolder springContextHolder;

    public void responseWrite(HttpServletResponse response, String content) throws IOException {
        response.setContentType("application/json");
        response.getWriter().write(content);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ResultObjectVO resultVO = new ResultObjectVO();
        resultVO.setCode(ResultVO.SUCCESS);
        if (handler!=null&&handler instanceof HandlerMethod&&toucan.getUserAuth().isEnabled()) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            UserAuth authAnnotation = method.getAnnotation(UserAuth.class);
            try {
                response.setCharacterEncoding(Charset.defaultCharset().name());

                if (authAnnotation != null) {
                    //由用户中心做权限判断
                    if (authAnnotation.verifyMethod() == UserAuth.VERIFYMETHOD_USER_AUTH) {
                        //拿到权限中台账号服务
                        FeignUserService feignUserService = springContextHolder.getBean(FeignUserService.class);
                        if (authAnnotation.login()) {
                            logger.info("权限HTTP请求头为" + toucan.getUserAuth().getHttpToucanAuthHeader());
                            String authHeader = request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader());
                            //ajax请求
                            if (authAnnotation.requestType() == UserAuth.REQUEST_JSON||authAnnotation.requestType() == UserAuth.REQUEST_AJAX) {
                                //JSON类型请求
                                RequestWrapper RequestWrapper = new RequestWrapper((HttpServletRequest) request);
                                String jsonBody = new String(RequestWrapper.body);
                                logger.info("recive param " + jsonBody);

                                if (StringUtils.isEmpty(authHeader)) {
                                    logger.warn("权限请求头为空 " + toucan.getUserAuth().getHttpToucanAuthHeader() + " : " + authHeader);
                                    resultVO.setCode(ResultVO.HTTPCODE_401);
                                    resultVO.setMsg("访问失败,请检查请求权限参数");
                                    //默认登录页面
                                    resultVO.setData(toucan.getUserAuth().getLoginPage());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                logger.info(" auth header " + authHeader);

                                if (authHeader.indexOf("lt") == -1) {
                                    logger.info("lt不能为空 " + jsonBody);
                                    resultVO.setCode(ResultVO.HTTPCODE_401);
                                    resultVO.setMsg("lt不能为空");
                                    //默认登录页面
                                    resultVO.setData(toucan.getUserAuth().getLoginPage());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                if (authHeader.indexOf("uid") == -1) {
                                    logger.info("uid不能为空 " + jsonBody);
                                    resultVO.setCode(ResultVO.HTTPCODE_401);
                                    resultVO.setMsg("uid不能为空");
                                    //默认登录页面
                                    resultVO.setData(toucan.getUserAuth().getLoginPage());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                String[] authHeaderArray = authHeader.split(";");
                                String uid = "-1";
                                String lt = "-1";
                                for (int i = 0; i < authHeaderArray.length; i++) {
                                    if (authHeaderArray[i].indexOf("uid=") != -1) {
                                        uid = authHeaderArray[i].split("=")[1];
                                    }
                                    if (authHeaderArray[i].indexOf("lt=") != -1) {
                                        lt = authHeaderArray[i].split("=")[1];
                                    }
                                }
                                if (StringUtils.equals(uid, "-1") || StringUtils.equals(lt, "-1")) {
                                    logger.info("请求头参数异常 " + authHeader);
                                    resultVO.setCode(ResultVO.HTTPCODE_401);
                                    resultVO.setMsg("请求头参数异常");
                                    //默认登录页面
                                    resultVO.setData(toucan.getUserAuth().getLoginPage());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                //在这里调用用户中心 判断登录
                                UserLoginVO queryUserLogin = new UserLoginVO();
                                try {
                                    queryUserLogin.setUserMainId(Long.parseLong(uid));
                                    queryUserLogin.setLoginToken(lt);
                                }catch(Exception e)
                                {
                                    logger.info("请求头参数异常 " + authHeader);
                                    resultVO.setCode(ResultVO.HTTPCODE_401);
                                    resultVO.setMsg("请求头参数异常");
                                    //默认登录页面
                                    resultVO.setData(toucan.getUserAuth().getLoginPage());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }

                                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generatorByUser(toucan.getAppCode(),uid,queryUserLogin);
                                ResultObjectVO resultObjectVO = feignUserService.isOnline(SignUtil.sign(requestJsonVO),requestJsonVO);
                                if (resultObjectVO.getCode() != ResultVO.SUCCESS
                                        || !(Boolean.valueOf(String.valueOf(resultObjectVO.getData())).booleanValue())) {
                                    logger.info("登录验证失败 " + authHeader);
                                    resultVO.setCode(ResultVO.HTTPCODE_401);
                                    resultVO.setMsg("登录超时,请重新登录");
                                    //默认登录页面
                                    resultVO.setData(toucan.getUserAuth().getLoginPage());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }

                            }

                            //如果是直接请求
                            if (authAnnotation.requestType() == UserAuth.REQUEST_FORM) {
                                if (StringUtils.isEmpty(authHeader)) {
                                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                                            + request.getContextPath() + "/" + toucan.getUserAuth().getLoginPage());
                                    return false;
                                }

                                String[] authHeaderArray = authHeader.split(";");
                                String uid = "-1";
                                String lt = "-1";
                                for (int i = 0; i < authHeaderArray.length; i++) {
                                    if (authHeaderArray[i].indexOf("uid=") != -1) {
                                        uid = authHeaderArray[i].split("=")[1];
                                    }
                                    if (authHeaderArray[i].indexOf("lt=") != -1) {
                                        lt = authHeaderArray[i].split("=")[1];
                                    }
                                }
                                if (StringUtils.equals(uid, "-1") || StringUtils.equals(lt, "-1")) {
                                    logger.info("请求头参数异常 " + authHeader);
                                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                                            + request.getContextPath() + "/" + toucan.getUserAuth().getLoginPage());
                                    return false;
                                }


                                //在这里调用权限中台 判断登录
                                UserLoginVO queryUserLongVO = new UserLoginVO();
                                queryUserLongVO.setUserMainId(Long.parseLong(uid));
                                queryUserLongVO.setLoginToken(lt);

                                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generatorByUser(toucan.getAppCode(),uid,queryUserLongVO);
                                ResultObjectVO resultObjectVO = feignUserService.isOnline(SignUtil.sign(requestJsonVO),requestJsonVO);
                                if (resultObjectVO.getCode() != ResultVO.SUCCESS
                                        || !(Boolean.valueOf(String.valueOf(resultObjectVO.getData())).booleanValue())) {
                                    logger.info("登录验证失败 " + authHeader);
                                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                                            + request.getContextPath() + "/" + toucan.getUserAuth().getLoginPage());
                                    return false;
                                }

                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                if (authAnnotation.requestType() == UserAuth.REQUEST_JSON||authAnnotation.requestType() == UserAuth.REQUEST_AJAX) {
                    resultVO.setCode(ResultVO.FAILD);
                    resultVO.setMsg("请求失败");
                    response.setContentType("application/json");
                    response.getWriter().write(JSONObject.toJSONString(resultVO));
                }
                if (authAnnotation.requestType() == UserAuth.REQUEST_FORM) {
                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                            + request.getContextPath() + "/" + toucan.getUserAuth().getLoginPage());
                }

                return false;
            }
        }

        return true;
    }
}
