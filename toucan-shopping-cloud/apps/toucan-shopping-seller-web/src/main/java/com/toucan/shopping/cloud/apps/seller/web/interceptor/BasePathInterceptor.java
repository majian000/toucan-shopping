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
        //用户注册页
        if(toucan.getShoppingPC()!=null&&toucan.getShoppingPC().getBasePath()!=null)
        {
            httpServletRequest.setAttribute("shoppingPcPath", toucan.getShoppingPC().getBasePath());
        }
        //商品审核预览页
        if(toucan.getShoppingPC()!=null&&toucan.getShoppingPC().getProductApprovePreviewPage()!=null)
        {
            httpServletRequest.setAttribute("productApprovePreviewPage", toucan.getShoppingPC().getProductApprovePreviewPage());
        }
        //商品详情页
        if(toucan.getShoppingPC()!=null&&toucan.getShoppingPC().getProductDetailPage()!=null)
        {
            httpServletRequest.setAttribute("productDetailPage", toucan.getShoppingPC().getProductDetailPage());
        }

        return true;
    }
}