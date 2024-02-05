package com.toucan.shopping.modules.log.web.filter;

import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.properties.modules.Modules;
import com.toucan.shopping.modules.common.properties.modules.log.Log;
import com.toucan.shopping.modules.common.properties.modules.log.Param;
import com.toucan.shopping.modules.common.properties.modules.log.Request;
import com.toucan.shopping.modules.common.properties.modules.log.Response;
import com.toucan.shopping.modules.log.queue.LogParamQueue;
import com.toucan.shopping.modules.log.vo.LogParam;
import com.toucan.shopping.modules.log.vo.LogParamHeader;
import com.toucan.shopping.modules.log.vo.LogParamRequest;
import com.toucan.shopping.modules.log.vo.LogParamResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class LogParamFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Toucan toucan; //核心配置

    private LogParamQueue logParamQueue; //参数队列

    private List<Pattern> ignoreUrlPatterns =new LinkedList<>();

    public List<Pattern> getIgnoreUrlPatterns() {
        return ignoreUrlPatterns;
    }

    public void setIgnoreUrlPatterns(List<Pattern> ignoreUrlPatterns) {
        this.ignoreUrlPatterns = ignoreUrlPatterns;
    }

    public Toucan getToucan() {
        return toucan;
    }

    public void setToucan(Toucan toucan) {
        this.toucan = toucan;
    }

    public LogParamQueue getLogParamQueue() {
        return logParamQueue;
    }

    public void setLogParamQueue(LogParamQueue logParamQueue) {
        this.logParamQueue = logParamQueue;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper resp = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(req, resp);

            Modules toucanModules = toucan.getModules();

            if(toucanModules!=null
                    &&toucanModules.getLog()!=null) {
                Log toucanLog = toucanModules.getLog();
                Param toucanParam = toucanLog.getParam();
                if(toucanParam!=null) {
                    Request requestConfig = toucanParam.getRequest();
                    LogParam logParam = null; //参数日志
                    if (requestConfig != null && requestConfig.isEnabled()) {
                        boolean isIgnore = false;
                        String uri = request.getRequestURI();
                        //判断是否忽略
                        if (CollectionUtils.isNotEmpty(requestConfig.getIgnoreUrls())) {
                            if (requestConfig.getIgnoreUrls().contains(uri)) {
                                isIgnore = true;
                            }
                        }


                        if (!isIgnore) {
                            logParam = new LogParam();
                            String contentType = request.getContentType();
                            List<String> contentTypeList = requestConfig.getContentTypeList();
                            if (CollectionUtils.isNotEmpty(contentTypeList)) {
                                LogParamRequest logParamRequest = new LogParamRequest();
                                logParamRequest.setUri(uri);
                                logParamRequest.setIp(request.getRemoteAddr());
                                logParamRequest.setMethod(request.getMethod());
                                logParamRequest.setContentType(contentType);
                                logParamRequest.setHeaders(new LinkedList<>());
                                Enumeration<String> headerNames = request.getHeaderNames();
                                while (headerNames.hasMoreElements()) {
                                    String headerName = headerNames.nextElement();
                                    LogParamHeader logParamHeader = new LogParamHeader();
                                    logParamHeader.setName(headerName);
                                    logParamHeader.setValue(request.getHeader(headerName));
                                    logParamRequest.getHeaders().add(logParamHeader);
                                }
                                if (StringUtils.isNotEmpty(contentType)) {
                                    for (String ct : contentTypeList) {
                                        if (contentType.indexOf(ct.toLowerCase()) != -1) {
                                            byte[] requestBody = req.getContentAsByteArray();
                                            logParamRequest.setBody(new String(requestBody, StandardCharsets.UTF_8));
                                        }
                                    }
                                } else {
                                    //表单提交
                                    if (contentTypeList.contains("application/x-www-form-urlencoded")) {
                                        if (request.getParameterMap() != null && request.getParameterMap().size() > 0) {
                                            StringBuilder builder = new StringBuilder();
                                            for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
                                                builder.append(entry.getKey());
                                                builder.append("=");
                                                String values[] = entry.getValue();
                                                if (values != null && values.length > 0) {
                                                    for (String v : values) {
                                                        builder.append(v);
                                                    }
                                                }
                                                builder.append(" ");
                                            }
                                            logParamRequest.setBody(builder.toString());
                                        }
                                    }
                                }

                                logParam.setRequest(logParamRequest);
                            }
                        }
                    }

                    Response responseConfig = toucanParam.getResponse();
                    if (responseConfig != null && responseConfig.isEnabled()) {

                        boolean isIgnore = false;
                        String uri = request.getRequestURI();
                        //判断是否忽略
                        if (CollectionUtils.isNotEmpty(responseConfig.getIgnoreUrls())) {
                            if (responseConfig.getIgnoreUrls().contains(uri)) {
                                isIgnore = true;
                            }
                        }
                        if (!isIgnore) {
                            if (logParam == null) {
                                logParam = new LogParam();
                            }
                            LogParamResponse logParamResponse = new LogParamResponse();
                            String contentType = request.getContentType();
                            logParamResponse.setUri(uri);
                            logParamResponse.setContentType(contentType);
                            if (StringUtils.isNotEmpty(contentType)) {
                                List<String> contentTypeList = responseConfig.getContentTypeList();
                                if (CollectionUtils.isNotEmpty(contentTypeList)) {
                                    for (String ct : contentTypeList) {
                                        if (contentType.indexOf(ct.toLowerCase()) != -1) {
                                            byte[] responseBody = resp.getContentAsByteArray();
                                            logParamResponse.setBody(new String(responseBody, StandardCharsets.UTF_8));
                                        }
                                    }
                                }
                            }
                            logParam.setResponse(logParamResponse);
                        }
                    }


                    if (logParam != null) {
                        logParamQueue.push(logParam);
                    }
                }
            }

        } finally {
            resp.copyBodyToResponse();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if(CollectionUtils.isNotEmpty(ignoreUrlPatterns))
        {
            String uri = request.getRequestURI();
            boolean matches = false;
            for(Pattern pattern:ignoreUrlPatterns)
            {
                matches = pattern.matcher(uri).matches();
                if(matches)
                {
                    return matches;
                }
            }
        }
        return super.shouldNotFilter(request);
    }
}
