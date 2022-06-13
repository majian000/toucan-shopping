package com.toucan.shopping.starter.admin.auth.log.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAuthService;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogVO;
import com.toucan.shopping.modules.admin.auth.vo.AuthVerifyVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.spring.context.SpringContextHolder;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.IPUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.common.wrapper.RequestWrapper;
import com.toucan.shopping.modules.common.wrapper.RequestXssWrapper;
import com.toucan.shopping.starter.admin.auth.log.queue.OperateLogQueue;
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
import java.util.Map;

/**
 * 操作日志
 */
@Component
public class OperateLogInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private OperateLogQueue operateLogQueue;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            if (handler instanceof HandlerMethod && toucan.getAdminAuth().getOperateLog().isEnabled()) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
                OperateLogVO operateLogVO = new OperateLogVO();
                operateLogVO.setIp(IPUtil.getRemoteAddr(request));
                operateLogVO.setAppCode(toucan.getAppCode());
                operateLogVO.setUri(request.getRequestURI());
                operateLogVO.setMethod(request.getMethod());
                operateLogVO.setCreateAdminId(AuthHeaderUtil.getAdminIdDefaultNull(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
                JSONObject jsonObject = null;
                //json请求
                if (request instanceof RequestWrapper ) {
                    RequestWrapper requestWrapper = (RequestWrapper)request;
                    if(requestWrapper.body!=null&&requestWrapper.body.length>0) {
                        jsonObject = JSONObject.parseObject(String.valueOf(requestWrapper.body));
                    }else{
                        jsonObject = JSONObject.parseObject(JSONObject.toJSONString(request.getParameterMap()));
                    }
                }else if (request instanceof RequestXssWrapper){
                    RequestXssWrapper requestXssWrapper = (RequestXssWrapper)request;
                    if(requestXssWrapper.body!=null&&requestXssWrapper.body.length>0) {
                        jsonObject = JSONObject.parseObject(String.valueOf(requestXssWrapper.body));
                    }else{
                        jsonObject = JSONObject.parseObject(JSONObject.toJSONString(request.getParameterMap()));
                    }
                } else {
                    jsonObject = JSONObject.parseObject(JSONObject.toJSONString(request.getParameterMap()));
                }

                if(jsonObject!=null) {
                    Map params = JSONObject.parseObject(JSONObject.toJSONString(jsonObject), Map.class);
                    if(params.get("password")!=null)
                    {
                        params.put("password","");  //手动替换掉
                    }
                    operateLogVO.setParams(JSONObject.toJSONString(params));
                }
                operateLogQueue.push(operateLogVO);

            }
        }catch(Exception e)
        {
            logger.error(e.getMessage(),e);
        }

        return true;
    }
}
