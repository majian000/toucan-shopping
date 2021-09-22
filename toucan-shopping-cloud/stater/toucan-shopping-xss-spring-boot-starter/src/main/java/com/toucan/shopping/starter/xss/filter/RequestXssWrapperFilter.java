package com.toucan.shopping.starter.xss.filter;

import com.toucan.shopping.modules.common.wrapper.RequestWrapper;
import com.toucan.shopping.modules.common.wrapper.RequestXssWrapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 将HttpServletRequets对象替换成BBSRequestWrapper对象
 */
@Data
public class RequestXssWrapperFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String[] excludePaths;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("初始化HttpRequest对象替换成RequestXssWrapper过滤器...............");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        if(excludePaths!=null&&excludePaths.length>0)
        {
            for(String path:excludePaths)
            {
                //在忽略的路径中,就直接忽略xss过滤
                if(httpServletRequest.getRequestURI().lastIndexOf(path)!=-1)
                {
                    filterChain.doFilter(httpServletRequest, servletResponse);
                    return;
                }

            }
        }

        ServletRequest requestWrapper = new RequestXssWrapper(httpServletRequest);
        filterChain.doFilter(requestWrapper, servletResponse);

    }

    @Override
    public void destroy() {
        logger.info("销毁HttpRequest对象替换成RequestXssWrapper过滤器...............");
    }
}
