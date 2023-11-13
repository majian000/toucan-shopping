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
import com.toucan.shopping.modules.user.redis.UserCenterLoginRedisKey;
import com.toucan.shopping.modules.user.util.LoginTokenUtil;
import com.toucan.shopping.modules.user.vo.UserLoginVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
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

    public void deleteCookies(HttpServletResponse response)
    {
        //UID
        Cookie uidCookie = new Cookie("tss_uid","-1");
        uidCookie.setPath("/");
        //立刻清除
        uidCookie.setMaxAge(0);
        response.addCookie(uidCookie);

        //TOKEN
        Cookie ltCookie = new Cookie("tss_lt", "-1");
        ltCookie.setPath("/");
        //立刻清除
        ltCookie.setMaxAge(0);
        response.addCookie(ltCookie);

        //权限 TOKEN
        Cookie latCookie = new Cookie("tss_lat", "-1");
        latCookie.setPath("/");
        //立刻清除
        latCookie.setMaxAge(0);
        response.addCookie(latCookie);
    }

    public void flushAuthToken(String LoginAuthToken,HttpServletResponse response)
    {
        //重新生成权限token 半小时内不再进行权限校验
        Cookie latCookie = new Cookie( "tss_lat", LoginAuthToken);
        latCookie.setPath("/");
        latCookie.setMaxAge(UserCenterLoginRedisKey.SUPER_TOKEN_CLIENT_SECOND); //30分钟过期
        response.addCookie(latCookie);
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
                        //拿到用户中心账号服务
                        FeignUserService feignUserService = springContextHolder.getBean(FeignUserService.class);
                        if (authAnnotation.login()) {
                            logger.info("权限HTTP请求头为" + toucan.getUserAuth().getHttpToucanAuthHeader());
                            String authHeader = request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader());
                            //ajax请求
                            if (authAnnotation.responseType() == UserAuth.RESPONSE_JSON) {
                                //JSON类型请求
                                RequestWrapper RequestWrapper = new RequestWrapper((HttpServletRequest) request);
                                String jsonBody = new String(RequestWrapper.body);
                                logger.info("recive param " + jsonBody);

                                if (StringUtils.isEmpty(authHeader)) {
                                    logger.warn("权限请求头为空 " + toucan.getUserAuth().getHttpToucanAuthHeader() + " : " + authHeader);
                                    resultVO.setCode(ResultVO.HTTPCODE_403);
                                    resultVO.setMsg("访问失败,请检查请求权限参数");
                                    //默认登录页面

                                    if(StringUtils.isNotEmpty(toucan.getUserAuth().getLoginPage())) {
                                        resultVO.setData(toucan.getUserAuth().getLoginPage());
                                    }
                                    response.setStatus(HttpStatus.OK.value());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                logger.info(" auth header " + authHeader);

                                if (authHeader.indexOf("tss_lt") == -1) {
                                    logger.info("tss_lt不能为空 " + jsonBody);
                                    resultVO.setCode(ResultVO.HTTPCODE_403);
                                    resultVO.setMsg("tss_lt不能为空");
                                    //默认登录页面

                                    if(StringUtils.isNotEmpty(toucan.getUserAuth().getLoginPage())) {
                                        resultVO.setData(toucan.getUserAuth().getLoginPage());
                                    }
                                    response.setStatus(HttpStatus.OK.value());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                if (authHeader.indexOf("tss_uid") == -1) {
                                    logger.info("tss_uid不能为空 " + jsonBody);
                                    resultVO.setCode(ResultVO.HTTPCODE_403);
                                    resultVO.setMsg("tss_uid不能为空");
                                    //默认登录页面

                                    if(StringUtils.isNotEmpty(toucan.getUserAuth().getLoginPage())) {
                                        resultVO.setData(toucan.getUserAuth().getLoginPage());
                                    }
                                    response.setStatus(HttpStatus.OK.value());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                String[] authHeaderArray = authHeader.split(";");
                                String uid = "-1";
                                String lt = "-1";
                                String lat = "-1";
                                for (int i = 0; i < authHeaderArray.length; i++) {
                                    if (authHeaderArray[i].indexOf("tss_uid=") != -1) {
                                        uid = authHeaderArray[i].split("=")[1];
                                    }
                                    if (authHeaderArray[i].indexOf("tss_lt=") != -1) {
                                        lt = authHeaderArray[i].split("=")[1];
                                    }
                                    if (authHeaderArray[i].indexOf("tss_lat=") != -1) {
                                        lat = authHeaderArray[i].split("=")[1];
                                    }
                                }
                                if (StringUtils.equals(uid, "-1") || StringUtils.equals(lt, "-1")) {
                                    logger.info("请求头参数异常 " + authHeader);
                                    resultVO.setCode(ResultVO.HTTPCODE_403);
                                    resultVO.setMsg("请求头参数异常");
                                    //默认登录页面

                                    if(StringUtils.isNotEmpty(toucan.getUserAuth().getLoginPage())) {
                                        resultVO.setData(toucan.getUserAuth().getLoginPage());
                                    }
                                    response.setStatus(HttpStatus.OK.value());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                //判断UserMainId和登录Token能否对应的上
                                String loginToken = LoginTokenUtil.generatorTokenByString(uid);
                                if(!loginToken.equals(lt))
                                {
                                    logger.info(" 校验用户ID和loginToken不一致 {} loginToken {}" ,authHeader,lt);
                                    resultVO.setCode(ResultVO.HTTPCODE_403);
                                    resultVO.setMsg("登录超时,请重新登录");
                                    resultVO.setData(toucan.getUserAuth().getLoginPage());
                                    response.setStatus(HttpStatus.OK.value());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                //判断当前Token和权限Token能否对应的上
                                String glat = LoginTokenUtil.generatorTokenByString(lt);
                                if(glat.equals(lat))
                                {
                                    return true;
                                }
                                //判断当前的token和userId是否对应的上
                                UserLoginVO queryUserLogin = new UserLoginVO();
                                try {
                                    queryUserLogin.setUserMainId(Long.parseLong(uid));
                                    queryUserLogin.setLoginToken(lt);

                                    //判断用户登录状态是否在线
                                    RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generatorByUser(toucan.getAppCode(),uid,queryUserLogin);
                                    ResultObjectVO resultObjectVO = feignUserService.verifyLoginTokenAndIsOnline(SignUtil.sign(requestJsonVO),requestJsonVO);
                                    if(!resultObjectVO.isSuccess())
                                    {
                                        logger.info(" 校验loginToken不一致 或用户会话超时 {} loginToken {}" ,authHeader,lt);
                                        resultVO.setCode(ResultVO.HTTPCODE_403);
                                        resultVO.setMsg("登录超时,请重新登录");
                                        resultVO.setData(toucan.getUserAuth().getLoginPage());
                                        response.setStatus(HttpStatus.OK.value());
                                        responseWrite(response, JSONObject.toJSONString(resultVO));
                                        return false;
                                    }

                                    flushAuthToken(glat,response);

                                }catch(Exception e)
                                {
                                    logger.info("请求头参数异常 " + authHeader);
                                    resultVO.setCode(ResultVO.HTTPCODE_403);
                                    resultVO.setMsg("请求头参数异常");
                                    //默认登录页面

                                    if(StringUtils.isNotEmpty(toucan.getUserAuth().getLoginPage())) {
                                        resultVO.setData(toucan.getUserAuth().getLoginPage());
                                    }
                                    response.setStatus(HttpStatus.OK.value());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }


                            }

                            //如果是直接请求
                            if (authAnnotation.responseType() == UserAuth.RESPONSE_FORM) {
                                if (StringUtils.isEmpty(authHeader)) {

                                    if(StringUtils.isNotEmpty(toucan.getUserAuth().getLoginPage())) {
                                        response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                                                + request.getContextPath() + "/" + toucan.getUserAuth().getLoginPage());
                                    }
                                    return false;
                                }

                                String[] authHeaderArray = authHeader.split(";");
                                String uid = "-1";
                                String lt = "-1";
                                String lat = "-1";
                                for (int i = 0; i < authHeaderArray.length; i++) {
                                    if (authHeaderArray[i].indexOf("tss_uid=") != -1) {
                                        uid = authHeaderArray[i].split("=")[1];
                                    }
                                    if (authHeaderArray[i].indexOf("tss_lt=") != -1) {
                                        lt = authHeaderArray[i].split("=")[1];
                                    }
                                    if (authHeaderArray[i].indexOf("tss_lat=") != -1) {
                                        lat = authHeaderArray[i].split("=")[1];
                                    }
                                }
                                if (StringUtils.equals(uid, "-1") || StringUtils.equals(lt, "-1")) {
                                    logger.warn("请求头参数异常 " + authHeader);
                                    //删除cookies
                                    deleteCookies(response);

                                    if(StringUtils.isNotEmpty(toucan.getUserAuth().getLoginPage())) {
                                        request.setAttribute("redirectUrl",request.getRequestURI());
                                        request.getRequestDispatcher(toucan.getUserAuth().getLoginPage()).forward(request, response);
                                    }
                                    return false;
                                }

                                //判断UserMainId和登录Token能否对应的上
                                String loginToken = LoginTokenUtil.generatorTokenByString(uid);
                                if(!loginToken.equals(lt))
                                {
                                    logger.warn("用户ID和登录Token不一致 header:{} uid:{} loginToken:{}",authHeader,uid,loginToken);
                                    //删除cookies
                                    deleteCookies(response);

                                    if(StringUtils.isNotEmpty(toucan.getUserAuth().getLoginPage())) {
                                        request.setAttribute("redirectUrl",request.getRequestURI());
                                        request.getRequestDispatcher(toucan.getUserAuth().getLoginPage()).forward(request, response);
                                    }
                                    return false;
                                }

                                //判断当前Token和权限Token能否对应的上
                                String glat = LoginTokenUtil.generatorTokenByString(lt);
                                if(glat.equals(lat))
                                {
                                    return true;
                                }

                                //在这里调用用户中心 判断登录
                                UserLoginVO queryUserLoginVO = new UserLoginVO();
                                queryUserLoginVO.setUserMainId(Long.parseLong(uid));
                                queryUserLoginVO.setLoginToken(lt);


                                //判断登录用户会话是否超时
                                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generatorByUser(toucan.getAppCode(),uid,queryUserLoginVO);
                                ResultObjectVO resultObjectVO = feignUserService.verifyLoginTokenAndIsOnline(SignUtil.sign(requestJsonVO),requestJsonVO);
                                if(!resultObjectVO.isSuccess())
                                {
                                    logger.info("登录token校验失败 {} loginToken {}" + authHeader,lt);

                                    //删除cookies
                                    deleteCookies(response);

                                    if(StringUtils.isNotEmpty(toucan.getUserAuth().getLoginPage())) {
                                        request.setAttribute("redirectUrl",request.getRequestURI());
                                        request.getRequestDispatcher(toucan.getUserAuth().getLoginPage()).forward(request, response);
                                    }
                                    return false;
                                }

                                flushAuthToken(glat,response);

                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                if (authAnnotation.responseType() == UserAuth.RESPONSE_JSON) {
                    resultVO.setCode(ResultVO.FAILD);
                    resultVO.setMsg("请求失败");
                    response.setContentType("application/json");
                    response.setStatus(HttpStatus.OK.value());
                    response.getWriter().write(JSONObject.toJSONString(resultVO));
                }
                if (authAnnotation.responseType() == UserAuth.RESPONSE_FORM) {
                    if(StringUtils.isNotEmpty(toucan.getUserAuth().getLoginPage())) {
                        response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                                + request.getContextPath() + "/" + toucan.getUserAuth().getLoginPage());
                    }
                }

                return false;
            }
        }

        return true;
    }
}
