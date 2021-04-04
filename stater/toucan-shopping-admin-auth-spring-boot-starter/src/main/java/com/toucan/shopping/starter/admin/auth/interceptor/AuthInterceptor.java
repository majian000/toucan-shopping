package com.toucan.shopping.starter.admin.auth.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.auth.user.Auth;
import com.toucan.shopping.user.api.feign.service.FeignUserService;
import com.toucan.shopping.user.export.vo.UserLoginVO;
import com.toucan.shopping.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.common.spring.context.SpringContextHolder;
import com.toucan.shopping.common.util.SignUtil;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.common.vo.ResultVO;
import com.toucan.shopping.common.wrapper.RequestWrapper;
import com.toucan.shopping.common.properties.Toucan;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

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
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Auth authAnnotation = method.getAnnotation(Auth.class);
            try {
                response.setCharacterEncoding(Charset.defaultCharset().name());

                if (authAnnotation != null) {
                    //由用户中心做权限判断
                    if (authAnnotation.verifyMethod() == Auth.VERIFYMETHOD_USER_CENTER) {
                        //拿到用户中心服务
                        FeignUserService feignUserService = springContextHolder.getBean(FeignUserService.class);
                        if (authAnnotation.login()) {
                            logger.info("权限HTTP请求头为" + toucan.getAdminAuth().getHttpBbsAuthHeader());
                            String authHeader = request.getHeader(toucan.getAdminAuth().getHttpBbsAuthHeader());
                            //ajax请求
                            if (authAnnotation.requestType() == Auth.REQUEST_JSON) {
                                //JSON类型请求
                                RequestWrapper RequestWrapper = new RequestWrapper((HttpServletRequest) request);
                                String jsonBody = new String(RequestWrapper.body);
                                logger.info("recive param " + jsonBody);

                                if (StringUtils.isEmpty(authHeader)) {
                                    logger.warn("权限请求头为空 " + toucan.getAdminAuth().getHttpBbsAuthHeader() + " : " + authHeader);
                                    resultVO.setCode(ResultVO.FAILD);
                                    resultVO.setMsg("访问失败,请检查请求权限参数");
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                logger.info(" auth header " + authHeader);

                                if (authHeader.indexOf("lt") == -1) {
                                    logger.info("lt不能为空 " + jsonBody);
                                    resultVO.setCode(ResultVO.FAILD);
                                    resultVO.setMsg("lt不能为空");
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                if (authHeader.indexOf("uid") == -1) {
                                    logger.info("uid不能为空 " + jsonBody);
                                    resultVO.setCode(ResultVO.FAILD);
                                    resultVO.setMsg("uid不能为空");
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
                                    resultVO.setCode(ResultVO.FAILD);
                                    resultVO.setMsg("请求头参数异常");
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                //在这里调用用户中心 判断登录
                                UserLoginVO queryUserLogin = new UserLoginVO();
                                queryUserLogin.setId(Long.parseLong(uid));
                                queryUserLogin.setLoginToken(lt);


                                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generatorByUser(toucan.getAppCode(),uid,queryUserLogin);
                                ResultObjectVO resultObjectVO = feignUserService.isOnline(SignUtil.sign(requestJsonVO),requestJsonVO);
                                if (resultObjectVO.getCode() != ResultVO.SUCCESS
                                        || !(Boolean.valueOf(String.valueOf(resultObjectVO.getData())).booleanValue())) {
                                    logger.info("登录验证失败 " + authHeader);
                                    resultVO.setCode(ResultVO.FAILD);
                                    resultVO.setMsg("登录超时,请重新登录");
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                            }

                            //如果是直接请求
                            if (authAnnotation.requestType() == Auth.REQUEST_FORM) {
                                if (StringUtils.isEmpty(authHeader)) {
                                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                                            + request.getContextPath() + "/" + toucan.getAdminAuth().getLoginPage());
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
                                            + request.getContextPath() + "/" + toucan.getAdminAuth().getLoginPage());
                                    return false;
                                }


                                //在这里调用用户中心 判断登录
                                UserLoginVO queryUserLogin = new UserLoginVO();
                                queryUserLogin.setId(Long.parseLong(uid));
                                queryUserLogin.setLoginToken(lt);

                                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generatorByUser(toucan.getAppCode(),uid,queryUserLogin);
                                ResultObjectVO resultObjectVO = feignUserService.isOnline(SignUtil.sign(requestJsonVO),requestJsonVO);
                                if (resultObjectVO.getCode() != ResultVO.SUCCESS
                                        || !(Boolean.valueOf(String.valueOf(resultObjectVO.getData())).booleanValue())) {
                                    logger.info("登录验证失败 " + authHeader);
                                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                                            + request.getContextPath() + "/" + toucan.getAdminAuth().getLoginPage());
                                    return false;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                if (authAnnotation.requestType() == Auth.REQUEST_JSON) {
                    resultVO.setCode(ResultVO.FAILD);
                    resultVO.setMsg("操作失败,请检查传入参数");
                    response.setContentType("application/json");
                    response.getWriter().write(JSONObject.toJSONString(resultVO));
                }
                if (authAnnotation.requestType() == Auth.REQUEST_FORM) {
                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                            + request.getContextPath() + "/" + toucan.getAdminAuth().getLoginPage());
                }

                return false;
            }
        }
        return true;
    }
}
