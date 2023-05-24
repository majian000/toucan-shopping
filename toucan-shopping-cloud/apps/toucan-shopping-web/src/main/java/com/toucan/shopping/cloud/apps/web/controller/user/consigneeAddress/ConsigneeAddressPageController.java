package com.toucan.shopping.cloud.apps.web.controller.user.consigneeAddress;


import com.alibaba.fastjson.JSONArray;
import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.service.LoginUserService;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignAreaService;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户收货地址
 */
@Controller("consigneeAddressPageController")
@RequestMapping("/page/user/consigneeAddress")
public class ConsigneeAddressPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private FeignAreaService feignAreaService;

    @UserAuth(requestType =UserAuth.REQUEST_FORM)
    @RequestMapping("/add")
    public String addPage(HttpServletRequest request)
    {
        return "user/consigneeAddress/add";
    }


    @UserAuth(requestType =UserAuth.REQUEST_FORM)
    @RequestMapping("/edit/{id}")
    public String editPage(HttpServletRequest request, @PathVariable String id)
    {
        request.setAttribute("id",String.valueOf(id));
        return "user/consigneeAddress/edit";
    }

    @UserAuth(requestType =UserAuth.REQUEST_FORM)
    @RequestMapping("/list")
    public String listPage(HttpServletRequest request)
    {
        return "user/consigneeAddress/list";
    }

}
