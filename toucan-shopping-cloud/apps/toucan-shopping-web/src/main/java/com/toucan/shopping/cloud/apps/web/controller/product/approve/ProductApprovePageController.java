package com.toucan.shopping.cloud.apps.web.controller.product.approve;

import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.modules.common.properties.Toucan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 商品审核页控制器
 * @auth majian
 * @date 2022-5-18 15:58:46
 */
@Controller("pageProductApproveController")
@RequestMapping("/page/product/approve")
public class ProductApprovePageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;



    @RequestMapping("/preview/{id}")
    public String page(@PathVariable String id)
    {
        return "product/approve/preview";
    }


}
