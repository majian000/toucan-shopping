package com.toucan.shopping.standard.apps.admin.auth.web.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PageNotFoundInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
            response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/index/page");
            return false;
        } else if (response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/index/page");
            return false;
        }
        return true;
    }

}