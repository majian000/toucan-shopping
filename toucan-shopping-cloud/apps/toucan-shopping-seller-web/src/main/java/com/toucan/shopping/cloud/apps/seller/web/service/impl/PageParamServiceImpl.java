package com.toucan.shopping.cloud.apps.seller.web.service.impl;

import com.toucan.shopping.cloud.apps.seller.web.service.PageParamService;
import com.toucan.shopping.modules.common.properties.Toucan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PageParamServiceImpl implements PageParamService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Override
    public Map<String, String> getPageCommonParams() {
        Map<String,String> params = new HashMap<>();
        //用户注册页
        if(toucan.getShoppingPC()!=null&&toucan.getShoppingPC().getBasePath()!=null)
        {
            params.put("shoppingPcPath", toucan.getShoppingPC().getBasePath());
        }else{
            params.put("shoppingPcPath","");
        }

        //商品审核预览页
        if(toucan.getShoppingPC()!=null&&toucan.getShoppingPC().getProductApprovePreviewPage()!=null)
        {
            params.put("productApprovePreviewPage", toucan.getShoppingPC().getProductApprovePreviewPage());
        }else{
            params.put("productApprovePreviewPage", "");
        }
        //商品详情页
        if(toucan.getShoppingPC()!=null&&toucan.getShoppingPC().getProductDetailPage()!=null)
        {
            params.put("productDetailPage", toucan.getShoppingPC().getProductDetailPage());
        }else{
            params.put("productDetailPage", "");
        }

        //商品预览页
        if(toucan.getShoppingPC()!=null&&toucan.getShoppingPC().getProductPreviewPage()!=null)
        {
            params.put("productPreviewPage", toucan.getShoppingPC().getProductPreviewPage());
        }else{
            params.put("productPreviewPage", "");
        }

        return params;
    }
}
