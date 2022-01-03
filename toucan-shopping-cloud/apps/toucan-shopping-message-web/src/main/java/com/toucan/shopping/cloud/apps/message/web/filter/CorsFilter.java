package com.toucan.shopping.cloud.apps.message.web.filter;

import com.toucan.shopping.modules.common.properties.Toucan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter implements Filter {

    @Autowired
    private Toucan toucan;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //访问来源
        String origin=((HttpServletRequest) servletRequest).getHeader("Origin");
        if(toucan.getMessageWebPC()!=null&&toucan.getMessageWebPC().getAllowedOrigins()!=null)
        {
            if(toucan.getMessageWebPC().getAllowedOrigins().contains(origin))
            {
                response.setHeader("Access-Control-Allow-Origin", origin);
                response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
                response.setHeader("Access-Control-Max-Age", "3600");
                response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization");
                response.setHeader("Access-Control-Allow-Credentials","true");
            }

        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}

