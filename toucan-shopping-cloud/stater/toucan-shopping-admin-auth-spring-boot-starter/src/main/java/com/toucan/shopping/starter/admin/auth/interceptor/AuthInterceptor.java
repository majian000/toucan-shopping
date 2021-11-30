package com.toucan.shopping.starter.admin.auth.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAuthService;
import com.toucan.shopping.modules.admin.auth.vo.AuthVerifyVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.spring.context.SpringContextHolder;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.common.wrapper.RequestWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

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


    /**
     * 校验权限
     * @param adminId
     * @param method
     * @return
     */
    public Integer authVerifyLoginAndUrl(String adminId, String loginToken, Class clazz, Method method) throws NoSuchAlgorithmException {
        AuthVerifyVO authVerifyVO = new AuthVerifyVO();
        authVerifyVO.setAdminId(adminId);
        authVerifyVO.setLoginToken(loginToken);
        String url="";
        //拿到控制器的路径
        RequestMapping controllerRequestMapping =((RequestMapping)clazz.getAnnotation(RequestMapping.class));
        if(controllerRequestMapping!=null)
        {
            if(controllerRequestMapping.value()!=null&&controllerRequestMapping.value().length>0) {
                url += controllerRequestMapping.value()[0];
            }
        }
        //拿到方法的路径
        RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
        if(methodRequestMapping==null)
        {
            PostMapping methodPostMapping = method.getAnnotation(PostMapping.class);
            if(methodPostMapping==null)
            {
                GetMapping methodGetMapping = method.getAnnotation(GetMapping.class);
                if(methodGetMapping==null)
                {
                    DeleteMapping methodDeleteMapping = method.getAnnotation(DeleteMapping.class);
                    if(methodDeleteMapping==null)
                    {
                        PutMapping methodPutMapping = method.getAnnotation(PutMapping.class);
                        if(methodPutMapping!=null)
                        {
                            if(methodPutMapping.value()!=null&&methodPutMapping.value().length>0) {
                                url += methodPutMapping.value()[0];
                            }
                        }
                    }else{
                        if(methodDeleteMapping.value()!=null&&methodDeleteMapping.value().length>0) {
                            url += methodDeleteMapping.value()[0];
                        }
                    }
                }else{
                    if(methodGetMapping.value()!=null&&methodGetMapping.value().length>0) {
                        url += methodGetMapping.value()[0];
                    }
                }
            }else{
                if(methodPostMapping.value()!=null&&methodPostMapping.value().length>0) {
                    url += methodPostMapping.value()[0];
                }
            }
        }else{
            if(methodRequestMapping.value()!=null&&methodRequestMapping.value().length>0) {
                url += methodRequestMapping.value()[0];
            }
        }

        //去掉地址传递的参数
        if(url.indexOf("{")!=-1)
        {
            url = url.substring(0,url.indexOf("{")-1);
        }

        authVerifyVO.setUrl(url);
        authVerifyVO.setAppCode(toucan.getAppCode());

        //这里可以优化,初始化的时候 传入这个bean
        FeignAuthService feignAuthService = springContextHolder.getBean(FeignAuthService.class);
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),authVerifyVO);
        ResultObjectVO resultObjectVO = feignAuthService.verifyLoginAndUrl(SignUtil.sign(requestJsonVO),requestJsonVO);

        //-1 登录超时 -2没有权限
        if(resultObjectVO.getData()!=null) {
            return resultObjectVO.formatData(Integer.class);
        }
        return -1;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ResultObjectVO resultVO = new ResultObjectVO();
        resultVO.setCode(ResultVO.SUCCESS);
        if (handler instanceof HandlerMethod&&toucan.getAdminAuth().isEnabled()) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            AdminAuth authAnnotation = method.getAnnotation(AdminAuth.class);
            try {
                response.setCharacterEncoding(Charset.defaultCharset().name());

                if (authAnnotation != null) {
                    //由权限中台做权限判断
                    if (authAnnotation.verifyMethod() == AdminAuth.VERIFYMETHOD_ADMIN_AUTH) {
                        //拿到权限中台账号服务
                        FeignAdminService feignAdminService = springContextHolder.getBean(FeignAdminService.class);
                        if (authAnnotation.login()) {
                            String aidKey = toucan.getAppCode()+"_aid=";
                            String ltKey = toucan.getAppCode()+"_lt=";

                            logger.info("权限HTTP请求头为" + toucan.getAdminAuth().getHttpToucanAuthHeader());
                            String authHeader = request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader());
                            //ajax请求
                            if (authAnnotation.requestType() == AdminAuth.REQUEST_JSON) {
                                logger.info("request uri {} " , request.getRequestURI());
                                //JSON类型请求
                                RequestWrapper RequestWrapper = new RequestWrapper((HttpServletRequest) request);
                                String jsonBody = new String(RequestWrapper.body);
                                logger.info("recive param {} " , jsonBody);

                                if (StringUtils.isEmpty(authHeader)) {
                                    logger.warn("权限请求头为空 " + toucan.getAdminAuth().getHttpToucanAuthHeader() + " : " + authHeader);
                                    resultVO.setCode(ResultVO.FAILD);
                                    resultVO.setMsg("访问失败,请检查请求权限参数");
                                    response.setStatus(HttpStatus.FORBIDDEN.value());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                logger.info(" auth header " + authHeader);

                                if (authHeader.indexOf(ltKey) == -1) {
                                    logger.info(ltKey+"不能为空 " + jsonBody);
                                    resultVO.setCode(ResultVO.FAILD);
                                    resultVO.setMsg(ltKey+"不能为空");
                                    response.setStatus(HttpStatus.FORBIDDEN.value());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                if (authHeader.indexOf(aidKey) == -1) {
                                    logger.info(aidKey+"不能为空 " + jsonBody);
                                    resultVO.setCode(ResultVO.FAILD);
                                    resultVO.setMsg(aidKey+"不能为空");
                                    response.setStatus(HttpStatus.FORBIDDEN.value());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                String[] authHeaderArray = authHeader.split(";");
                                String aid = "-1";
                                String lt = "-1";
                                for (int i = 0; i < authHeaderArray.length; i++) {
                                    if (authHeaderArray[i].indexOf(aidKey) != -1) {
                                        aid = authHeaderArray[i].split("=")[1];
                                    }
                                    if (authHeaderArray[i].indexOf(ltKey) != -1) {
                                        lt = authHeaderArray[i].split("=")[1];
                                    }
                                }
                                if (StringUtils.equals(aid, "-1") || StringUtils.equals(lt, "-1")) {
                                    logger.info("请求头参数异常 " + authHeader);
                                    resultVO.setCode(ResultVO.FAILD);
                                    resultVO.setMsg("请求头参数异常");
                                    response.setStatus(HttpStatus.FORBIDDEN.value());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                //在这里调用权限中台 判断登录
                                int verifyRet = authVerifyLoginAndUrl(aid,lt,handlerMethod.getBeanType(),method);
                                if (verifyRet==-1) {
                                    logger.info("登录验证失败 " + authHeader);
                                    resultVO.setCode(ResultVO.FAILD);
                                    resultVO.setMsg("登录超时,请重新登录");
                                    response.setStatus(HttpStatus.FORBIDDEN.value());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }


                                //校验请求权限
                                if(verifyRet==-2)
                                {
                                    logger.info("权限校验失败 " + authHeader);
                                    resultVO.setCode(ResultVO.FAILD);
                                    resultVO.setMsg("没有权限访问");
                                    response.setStatus(HttpStatus.FORBIDDEN.value());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                            }

                            //如果是直接请求
                            if (authAnnotation.requestType() == AdminAuth.REQUEST_FORM) {
                                if (StringUtils.isEmpty(authHeader)) {
                                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                                            + request.getContextPath() + "/" + toucan.getAdminAuth().getLoginPage());
                                    return false;
                                }

                                String[] authHeaderArray = authHeader.split(";");
                                String aid = "-1";
                                String lt = "-1";
                                for (int i = 0; i < authHeaderArray.length; i++) {
                                    if (authHeaderArray[i].indexOf(aidKey) != -1) {
                                        aid = authHeaderArray[i].split("=")[1];
                                    }
                                    if (authHeaderArray[i].indexOf(ltKey) != -1) {
                                        lt = authHeaderArray[i].split("=")[1];
                                    }
                                }
                                if (StringUtils.equals(aid, "-1") || StringUtils.equals(lt, "-1")) {
                                    logger.info("请求头参数异常 " + authHeader);
                                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                                            + request.getContextPath() + "/" + toucan.getAdminAuth().getLoginPage());
                                    return false;
                                }


                                //在这里调用权限中台 判断登录
                                int verifyRet = authVerifyLoginAndUrl(aid,lt,handlerMethod.getBeanType(),method);
                                if (verifyRet==-1) {
                                    logger.info("登录验证失败 " + authHeader);
                                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                                            + request.getContextPath() + "/" + toucan.getAdminAuth().getLoginPage());
                                    return false;
                                }

                                //校验请求权限
                                if(verifyRet==-2)
                                {
                                    logger.info("权限校验失败 " + authHeader);
                                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                                            + request.getContextPath() + "/" + toucan.getAdminAuth().getPage403());
                                    return false;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                if (authAnnotation.requestType() == AdminAuth.REQUEST_JSON) {
                    resultVO.setCode(ResultVO.FAILD);
                    resultVO.setMsg("请求失败");
                    response.setContentType("application/json");
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write(JSONObject.toJSONString(resultVO));
                }
                if (authAnnotation.requestType() == AdminAuth.REQUEST_FORM) {
                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                            + request.getContextPath() + "/" + toucan.getAdminAuth().getLoginPage());
                }

                return false;
            }
        }

        return true;
    }
}
