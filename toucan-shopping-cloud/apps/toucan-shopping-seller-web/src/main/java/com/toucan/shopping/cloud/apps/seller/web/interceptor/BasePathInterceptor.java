package com.toucan.shopping.cloud.apps.seller.web.interceptor;

import com.toucan.shopping.modules.common.properties.Toucan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BasePathInterceptor implements HandlerInterceptor {

    @Autowired
    private Toucan toucan;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        httpServletRequest.setAttribute("basePath", httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() + httpServletRequest.getContextPath());
        if(toucan.getShoppingPC()!=null&&toucan.getShoppingPC().getBasePath()!=null)
        {
            httpServletRequest.setAttribute("shoppingPcPath", toucan.getShoppingPC().getBasePath());
        }
        return true;
    }
}