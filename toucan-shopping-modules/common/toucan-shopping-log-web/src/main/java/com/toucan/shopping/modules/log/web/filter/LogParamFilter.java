package com.toucan.shopping.modules.log.web.filter;

import com.toucan.shopping.modules.common.properties.Toucan;
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
                    &&toucan.getModules().getLog()!=null
                    &&toucan.getModules().getLog().getRequest()!=null
                    &&toucan.getModules().getLog().getRequest().isEnabled()) {
                String contentType = request.getContentType();
                if(StringUtils.isNotEmpty(contentType)) {
                    List<String> contentTypeList = toucan.getModules().getLog().getRequest().getContentTypeList();
                    if (CollectionUtils.isNotEmpty(contentTypeList)) {
                        for(String ct:contentTypeList) {
                            if (contentType.indexOf(ct.toLowerCase())!=-1) {
                                byte[] requestBody = req.getContentAsByteArray();
                                logger.info("request uri:{} method:{} body:{}", request.getRequestURI(), request.getMethod(), new String(requestBody, StandardCharsets.UTF_8));
                            }
                        }
                    }
                }
            }

            if(toucan.getModules()!=null
                    &&toucan.getModules().getLog()!=null
                    &&toucan.getModules().getLog().getResponse()!=null
                    &&toucan.getModules().getLog().getResponse().isEnabled()) {
                String contentType = request.getContentType();
                if(StringUtils.isNotEmpty(contentType)) {
                    List<String> contentTypeList = toucan.getModules().getLog().getResponse().getContentTypeList();
                    if(CollectionUtils.isNotEmpty(contentTypeList)) {
                        for(String ct:contentTypeList) {
                            if (contentType.indexOf(ct.toLowerCase())!=-1) {
                                byte[] responseBody = resp.getContentAsByteArray();
                                logger.info("response uri:{} body:{}", request.getRequestURI(),new String(responseBody, StandardCharsets.UTF_8));
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
