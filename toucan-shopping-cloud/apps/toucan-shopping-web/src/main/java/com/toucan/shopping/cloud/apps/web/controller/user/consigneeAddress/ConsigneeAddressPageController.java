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

    @UserAuth
    @RequestMapping("/add")
    public String addPage(HttpServletRequest request)
    {
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), new AreaVO());
            ResultObjectVO resultObjectVO = feignAreaService.queryFullCache(requestJsonVO.sign(), requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                request.setAttribute("areaList", JSONArray.toJSONString(resultObjectVO.getData()));
            }else{
                request.setAttribute("areaList","[]");
            }
        }catch(Exception e)
        {
            request.setAttribute("areaList","[]");
            logger.warn("查询地区缓存失败 {} ",e.getMessage());
            logger.warn(e.getMessage(),e);
        }
        return "user/consigneeAddress/add";
    }


}