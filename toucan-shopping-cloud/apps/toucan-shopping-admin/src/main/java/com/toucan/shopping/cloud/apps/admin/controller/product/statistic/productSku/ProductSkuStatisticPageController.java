package com.toucan.shopping.cloud.apps.admin.controller.product.statistic.productSku;

import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.properties.Toucan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 商品SKU统计 - 页面控制器
 */
@Controller
public class ProductSkuStatisticPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/productSkuStatistic/categoryStatisticPage", method = RequestMethod.GET)
    public String listPage(HttpServletRequest request) {
        super.initButtons(request, toucan, "/productSkuStatistic/categoryStatisticPage", functionServiceAPI);
        return "pages/product/statistic/productSku/statistic_list.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/productSkuStatistic/hotSellStatisticPage", method = RequestMethod.GET)
    public String hotSellStatisticPage(HttpServletRequest request) {
        super.initButtons(request, toucan, "/productSkuStatistic/hotSellStatisticPage", functionServiceAPI);
        return "pages/product/statistic/productSku/hot_sell_statistic_list.html";
    }

}
