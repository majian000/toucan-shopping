package com.toucan.shopping.starter.admin.auth.filter;

import com.toucan.shopping.modules.common.wrapper.RequestWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 将HttpServletRequets对象替换成BBSRequestWrapper对象
 */
public class RequestWrapperFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("初始化HttpRequest对象替换成RequestWrapper过滤器...............");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        String contentType=httpServletRequest.getHeader("Content-Type");
        if(StringUtils.isNotEmpty(contentType)&&contentType.indexOf("application/json")!=-1)
        {
            ServletRequest requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
            filterChain.doFilter(requestWrapper, servletResponse);
        }else {
            filterChain.doFilter(httpServletRequest, servletResponse);
        }

    }

    @Override
    public void destroy() {
        logger.info("销毁HttpRequest对象替换成RequestWrapper过滤器...............");
    }
}
