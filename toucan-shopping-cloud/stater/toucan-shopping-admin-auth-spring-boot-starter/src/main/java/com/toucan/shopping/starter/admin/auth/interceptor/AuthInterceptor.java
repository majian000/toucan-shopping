package com.toucan.shopping.starter.admin.auth.interceptor;


import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AuthServiceAPI;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

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
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(content);
    }

    /**
     * 从已缓存的RequestWrapper中读取请求body,不消费InputStream
     * 遍历HttpServletRequestWrapper链找到RequestWrapper,直接读取其缓存的body字段
     */
    private String getCachedRequestBody(HttpServletRequest request) {
        // 先尝试从最外层的RequestWrapper读取(如果当前请求就是)
        if (request instanceof RequestWrapper) {
            RequestWrapper rw = (RequestWrapper) request;
            return new String(rw.body);
        }
        // 遍历wrapper链查找RequestWrapper
        jakarta.servlet.ServletRequest current = request;
        while (current instanceof jakarta.servlet.http.HttpServletRequestWrapper) {
            jakarta.servlet.http.HttpServletRequestWrapper wrapper = (jakarta.servlet.http.HttpServletRequestWrapper) current;
            jakarta.servlet.ServletRequest inner = wrapper.getRequest();
            if (inner instanceof RequestWrapper) {
                RequestWrapper rw = (RequestWrapper) inner;
                return new String(rw.body);
            }
            current = inner;
        }
        // 兜底: 如果找不到缓存的RequestWrapper,尝试直接读取(可能会消费流)
        try {
            RequestWrapper rw = new RequestWrapper(request);
            return new String(rw.body);
        } catch (Exception e) {
            logger.warn("读取请求body失败: {}", e.getMessage());
            return "";
        }
    }


    /**
     * 校验权限
     *
     * @param adminId
     * @param method
     * @return
     */
    public Integer authVerifyLoginAndUrl(String adminId, String loginToken, Class clazz, Method method) throws NoSuchAlgorithmException {
        AuthVerifyVO authVerifyVO = new AuthVerifyVO();
        authVerifyVO.setAdminId(adminId);
        authVerifyVO.setLoginToken(loginToken);
        String url = "";
        //拿到控制器的路径
        RequestMapping controllerRequestMapping = ((RequestMapping) clazz.getAnnotation(RequestMapping.class));
        if (controllerRequestMapping != null) {
            if (controllerRequestMapping.value() != null && controllerRequestMapping.value().length > 0) {
                url += controllerRequestMapping.value()[0];
            }
        }
        //拿到方法的路径
        RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
        if (methodRequestMapping == null) {
            PostMapping methodPostMapping = method.getAnnotation(PostMapping.class);
            if (methodPostMapping == null) {
                GetMapping methodGetMapping = method.getAnnotation(GetMapping.class);
                if (methodGetMapping == null) {
                    DeleteMapping methodDeleteMapping = method.getAnnotation(DeleteMapping.class);
                    if (methodDeleteMapping == null) {
                        PutMapping methodPutMapping = method.getAnnotation(PutMapping.class);
                        if (methodPutMapping != null) {
                            if (methodPutMapping.value() != null && methodPutMapping.value().length > 0) {
                                url += methodPutMapping.value()[0];
                            }
                        }
                    } else {
                        if (methodDeleteMapping.value() != null && methodDeleteMapping.value().length > 0) {
                            url += methodDeleteMapping.value()[0];
                        }
                    }
                } else {
                    if (methodGetMapping.value() != null && methodGetMapping.value().length > 0) {
                        url += methodGetMapping.value()[0];
                    }
                }
            } else {
                if (methodPostMapping.value() != null && methodPostMapping.value().length > 0) {
                    url += methodPostMapping.value()[0];
                }
            }
        } else {
            if (methodRequestMapping.value() != null && methodRequestMapping.value().length > 0) {
                url += methodRequestMapping.value()[0];
            }
        }

        //去掉地址传递的参数
        if (url.indexOf("{") != -1) {
            url = url.substring(0, url.indexOf("{") - 1);
        }

        authVerifyVO.setUrl(url);
        authVerifyVO.setAppCode(toucan.getAppCode());

        //这里可以优化,初始化的时候 传入这个bean
        AuthServiceAPI authServiceAPI = springContextHolder.getBean(AuthServiceAPI.class);
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), authVerifyVO);
        ResultObjectVO resultObjectVO = authServiceAPI.verifyLoginAndUrl(requestJsonVO);

        //-1 登录超时 -2没有权限
        if (resultObjectVO.getData() != null) {
            return resultObjectVO.formatData(Integer.class);
        }
        return -1;
    }


    /**
     * 校验权限标识
     *
     * @param adminId
     * @param loginToken
     * @param permissions
     * @return 1成功 / -1登录超时 / -2无权限
     */
    public Integer authVerifyPermission(String adminId, String loginToken, String[] permissions) throws NoSuchAlgorithmException {
        AuthVerifyVO authVerifyVO = new AuthVerifyVO();
        authVerifyVO.setAdminId(adminId);
        authVerifyVO.setLoginToken(loginToken);
        authVerifyVO.setAppCode(toucan.getAppCode());
        authVerifyVO.setPermissions(permissions);

        AuthServiceAPI authServiceAPI = springContextHolder.getBean(AuthServiceAPI.class);
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), authVerifyVO);
        ResultObjectVO resultObjectVO = authServiceAPI.verify(requestJsonVO);

        if (resultObjectVO.getData() != null) {
            if (resultObjectVO.getData() instanceof Boolean) {
                return Boolean.TRUE.equals(resultObjectVO.getData()) ? 1 : -2;
            }
        }
        return -2;
    }


    /**
     * 根据注解的verifyType进行统一校验
     *
     * @param adminId
     * @param loginToken
     * @param verifyType  1=ANY 2=URL 3=PERMISSION
     * @param permissions 权限标识数组
     * @param clazz       Controller类
     * @param method      Controller方法
     * @return 1成功 / -1登录超时 / -2无权限
     */
    public Integer doVerify(String adminId, String loginToken, int verifyType, String[] permissions,
                            Class clazz, Method method) throws Exception {
        if (verifyType == AdminAuth.VERIFY_TYPE_PERMISSION) {
            return authVerifyPermission(adminId, loginToken, permissions);
        }
        if (verifyType == AdminAuth.VERIFY_TYPE_ANY) {
            int urlRet = authVerifyLoginAndUrl(adminId, loginToken, clazz, method);
            if (urlRet == 1) {
                return 1;
            }
            if (permissions != null && permissions.length > 0) {
                int permRet = authVerifyPermission(adminId, loginToken, permissions);
                if (permRet == 1) {
                    return 1;
                }
                return urlRet;
            }
            return urlRet;
        }
        // 默认 VERIFY_TYPE_URL
        return authVerifyLoginAndUrl(adminId, loginToken, clazz, method);
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ResultObjectVO resultVO = new ResultObjectVO();
        resultVO.setCode(ResultVO.SUCCESS);

        String authHeader = request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader());

        String aidKey = toucan.getAppCode() + "_aid=";
        String ltKey = toucan.getAppCode() + "_lt=";
        String aid = "-1";
        String lt = "-1";

        if (StringUtils.isNotEmpty(authHeader)) {
            String[] authHeaderArray = authHeader.split(";");
            for (int i = 0; i < authHeaderArray.length; i++) {
                if (authHeaderArray[i].indexOf(aidKey) != -1) {
                    aid = authHeaderArray[i].split("=")[1];
                }
                if (authHeaderArray[i].indexOf(ltKey) != -1) {
                    lt = authHeaderArray[i].split("=")[1];
                }
            }
            AdminLoginHolder.setAdminLoginContext(aid, lt);
        }

        // Cookie中没有有效的认证信息时,尝试从Bearer Token解析
        if (StringUtils.equals(aid, "-1") || StringUtils.equals(lt, "-1")) {
            String bearerHeader = request.getHeader("Authorization");
            if (StringUtils.isNotEmpty(bearerHeader) && bearerHeader.startsWith("Bearer ")) {
                String token = bearerHeader.substring(7);
                String[] parts = token.split(":");
                if (parts.length == 2) {
                    aid = parts[0];
                    lt = parts[1];
                    AdminLoginHolder.setAdminLoginContext(aid, lt);
                }
            }
        }

        if (handler instanceof HandlerMethod && toucan.getAdminAuth().isEnabled()) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            AdminAuth authAnnotation = method.getAnnotation(AdminAuth.class);
            try {
                response.setCharacterEncoding(Charset.defaultCharset().name());

                if (authAnnotation != null) {
                    //由权限中台做权限判断
                    if (authAnnotation.verifyMethod() == AdminAuth.VERIFYMETHOD_ADMIN_AUTH) {
                        //拿到权限中台账号服务
                        if (authAnnotation.login()) {

                            logger.info("权限HTTP请求头为" + toucan.getAdminAuth().getHttpToucanAuthHeader());

                            //ajax请求
                            if (authAnnotation.responseType() == AdminAuth.RESPONSE_JSON) {
                                logger.info("request uri {} ", request.getRequestURI());
                                //JSON类型请求 - 从已缓存的RequestWrapper中读取body,避免重复消费InputStream
                                String jsonBody = getCachedRequestBody(request);
                                logger.info("recive param {} ", jsonBody);

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
                                    logger.info(ltKey + "不能为空 " + jsonBody);
                                    resultVO.setCode(ResultVO.FAILD);
                                    resultVO.setMsg(ltKey + "不能为空");
                                    response.setStatus(HttpStatus.FORBIDDEN.value());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                if (authHeader.indexOf(aidKey) == -1) {
                                    logger.info(aidKey + "不能为空 " + jsonBody);
                                    resultVO.setCode(ResultVO.FAILD);
                                    resultVO.setMsg(aidKey + "不能为空");
                                    response.setStatus(HttpStatus.FORBIDDEN.value());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                if (StringUtils.equals(aid, "-1") || StringUtils.equals(lt, "-1")) {
                                    logger.info("请求头参数异常 " + authHeader);
                                    resultVO.setCode(ResultVO.FAILD);
                                    resultVO.setMsg("请求头参数异常");
                                    response.setStatus(HttpStatus.FORBIDDEN.value());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                                // 根据verifyType进行校验(URL/权限标识/任意)
                                int verifyRet = doVerify(aid, lt,
                                        authAnnotation.verifyType(),
                                        authAnnotation.permissions(),
                                        handlerMethod.getBeanType(), method);
                                if (verifyRet == -1) {
                                    logger.info("登录验证失败 " + authHeader);
                                    resultVO.setCode(ResultVO.FAILD);
                                    resultVO.setMsg("登录超时,请重新登录");
                                    response.setStatus(HttpStatus.FORBIDDEN.value());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }


                                //校验请求权限
                                if (verifyRet == -2) {
                                    logger.info("权限校验失败 " + authHeader);
                                    resultVO.setCode(ResultVO.FAILD);
                                    resultVO.setMsg("没有权限访问");
                                    response.setStatus(HttpStatus.FORBIDDEN.value());
                                    responseWrite(response, JSONObject.toJSONString(resultVO));
                                    return false;
                                }
                            }

                            //如果是直接请求
                            if (authAnnotation.responseType() == AdminAuth.RESPONSE_FORM) {
                                if (StringUtils.isEmpty(authHeader)) {
                                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                                            + request.getContextPath() + "/" + toucan.getAdminAuth().getLoginPage());
                                    return false;
                                }


                                if (StringUtils.equals(aid, "-1") || StringUtils.equals(lt, "-1")) {
                                    logger.info("请求头参数异常 " + authHeader);
                                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                                            + request.getContextPath() + "/" + toucan.getAdminAuth().getLoginPage());
                                    return false;
                                }


                                // 根据verifyType进行校验(URL/权限标识/任意)
                                int verifyRet = doVerify(aid, lt,
                                        authAnnotation.verifyType(),
                                        authAnnotation.permissions(),
                                        handlerMethod.getBeanType(), method);
                                if (verifyRet == -1) {
                                    logger.info("登录验证失败 " + authHeader);
                                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                                            + request.getContextPath() + "/" + toucan.getAdminAuth().getLoginPage());
                                    return false;
                                }

                                //校验请求权限
                                if (verifyRet == -2) {
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
                if (authAnnotation.responseType() == AdminAuth.RESPONSE_JSON) {
                    resultVO.setCode(ResultVO.FAILD);
                    resultVO.setMsg("请求失败");
                    response.setContentType("application/json");
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write(JSONObject.toJSONString(resultVO));
                }
                if (authAnnotation.responseType() == AdminAuth.RESPONSE_FORM) {
                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                            + request.getContextPath() + "/" + toucan.getAdminAuth().getLoginPage());
                }

                return false;
            }
        }

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AdminLoginHolder.clear();
    }


}
