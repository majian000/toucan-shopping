package com.toucan.shopping.starter.admin.auth.log.interceptor;


import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogVO;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.IPUtil;
import com.toucan.shopping.modules.common.wrapper.RequestWrapper;
import com.toucan.shopping.modules.common.wrapper.RequestXssWrapper;
import com.toucan.shopping.starter.admin.auth.log.queue.OperateLogQueue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.ServletRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
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

    /**
     * 遍历包装链获取缓存的请求体
     */
    private String getCachedRequestBody(HttpServletRequest request) {
        // 直接判断当前request
        if (request instanceof RequestWrapper) {
            return new String(((RequestWrapper) request).body);
        }
        if (request instanceof RequestXssWrapper && ((RequestXssWrapper) request).body != null) {
            return new String(((RequestXssWrapper) request).body);
        }
        if (request instanceof ContentCachingRequestWrapper) {
            byte[] buf = ((ContentCachingRequestWrapper) request).getContentAsByteArray();
            if (buf.length > 0) {
                return new String(buf, Charset.defaultCharset());
            }
        }
        // 遍历包装链
        HttpServletRequest current = request;
        while (current instanceof ServletRequestWrapper) {
            current = (HttpServletRequest) ((ServletRequestWrapper) current).getRequest();
            if (current instanceof RequestWrapper) {
                return new String(((RequestWrapper) current).body);
            }
            if (current instanceof RequestXssWrapper && ((RequestXssWrapper) current).body != null) {
                return new String(((RequestXssWrapper) current).body);
            }
            if (current instanceof ContentCachingRequestWrapper) {
                byte[] buf = ((ContentCachingRequestWrapper) current).getContentAsByteArray();
                if (buf.length > 0) {
                    return new String(buf, Charset.defaultCharset());
                }
            }
        }
        return null;
    }

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

                // 优先从缓存的请求体中获取参数
                String cachedBody = getCachedRequestBody(request);
                if (StringUtils.isNotEmpty(cachedBody) && cachedBody.startsWith("{")) {
                    jsonObject = JSONObject.parseObject(cachedBody);
                } else {
                    // 回退到getParameterMap
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
