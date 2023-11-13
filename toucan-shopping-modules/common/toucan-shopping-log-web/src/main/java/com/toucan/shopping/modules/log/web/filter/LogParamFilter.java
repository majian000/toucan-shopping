package com.toucan.shopping.modules.log.web.filter;

import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.properties.modules.log.Request;
import com.toucan.shopping.modules.common.properties.modules.log.Response;
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
import java.util.List;
import java.util.Map;


public class LogParamFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    public Toucan getToucan() {
        return toucan;
    }

    public void setToucan(Toucan toucan) {
        this.toucan = toucan;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper resp = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(req, resp);


            if(toucan.getModules()!=null
                    &&toucan.getModules().getLog()!=null) {
                Request requestConfig = toucan.getModules().getLog().getRequest();
                if(requestConfig!=null&&requestConfig.isEnabled())
                {
                    boolean isIgnore=false;
                    String uri = request.getRequestURI();
                    //判断是否忽略
                    if(CollectionUtils.isNotEmpty(requestConfig.getIgnoreUrls()))
                    {
                        if(requestConfig.getIgnoreUrls().contains(uri))
                        {
                            isIgnore=true;
                        }
                    }

                    if(!isIgnore) {
                        String contentType = request.getContentType();
                        List<String> contentTypeList = requestConfig.getContentTypeList();
                        if (CollectionUtils.isNotEmpty(contentTypeList)) {
                            if (StringUtils.isNotEmpty(contentType)) {
                                for (String ct : contentTypeList) {
                                    if (contentType.indexOf(ct.toLowerCase()) != -1) {
                                        byte[] requestBody = req.getContentAsByteArray();
                                        logger.info("request uri:{} requestIp:{} method:{} body:{}", uri, request.getRemoteAddr(), request.getMethod(), new String(requestBody, StandardCharsets.UTF_8));
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
                                        logger.info("request uri:{} requestIp:{} method:{} body:{}", uri, request.getRemoteAddr(), request.getMethod(), builder.toString());
                                    }
                                }
                            }
                        }
                    }
                }

                Response responseConfig = toucan.getModules().getLog().getResponse();
                if(responseConfig!=null&&responseConfig.isEnabled()) {

                    boolean isIgnore=false;
                    String uri = request.getRequestURI();
                    //判断是否忽略
                    if(CollectionUtils.isNotEmpty(responseConfig.getIgnoreUrls()))
                    {
                        if(responseConfig.getIgnoreUrls().contains(uri))
                        {
                            isIgnore=true;
                        }
                    }
                    if(!isIgnore) {
                        String contentType = request.getContentType();
                        if (StringUtils.isNotEmpty(contentType)) {
                            List<String> contentTypeList = responseConfig.getContentTypeList();
                            if (CollectionUtils.isNotEmpty(contentTypeList)) {
                                for (String ct : contentTypeList) {
                                    if (contentType.indexOf(ct.toLowerCase()) != -1) {
                                        byte[] responseBody = resp.getContentAsByteArray();
                                        logger.info("response uri:{} requestIp:{} body:{}", uri, request.getRemoteAddr(), new String(responseBody, StandardCharsets.UTF_8));
                                    }
                                }
                            }
                        }
                    }
                }

            }

        } finally {
            resp.copyBodyToResponse();
        }
    }
}
