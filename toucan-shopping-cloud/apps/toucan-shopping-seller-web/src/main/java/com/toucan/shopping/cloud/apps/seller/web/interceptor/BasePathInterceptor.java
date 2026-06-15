package com.toucan.shopping.cloud.apps.seller.web.interceptor;

import com.toucan.shopping.cloud.apps.seller.web.service.PageParamService;
import com.toucan.shopping.modules.common.properties.Toucan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

@Component
public class BasePathInterceptor implements HandlerInterceptor {

    @Autowired
    private Toucan toucan;

    @Autowired
    private PageParamService pageParamService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        httpServletRequest.setAttribute("basePath", httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() + httpServletRequest.getContextPath());
        Map<String,String> pageParams= pageParamService.getPageCommonParams();
        Set<String> keys = pageParams.keySet();
        for(String key:keys){
            httpServletRequest.setAttribute(key,pageParams.get(key));
        }

        return true;
    }
}