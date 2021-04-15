package com.toucan.shopping.standard.modules.apps.web.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PageNotFoundInterceptor implements HandlerInterceptor {


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

        if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
            response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/index");
        } else if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
            response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/index");
        }
    }
}