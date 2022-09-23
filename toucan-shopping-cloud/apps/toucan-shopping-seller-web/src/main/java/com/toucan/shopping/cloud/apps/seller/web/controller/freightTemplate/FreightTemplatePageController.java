package com.toucan.shopping.cloud.apps.seller.web.controller.freightTemplate;

import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.seller.web.service.CategoryService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.properties.Toucan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * 运费模板
 */
@Controller("freightTemplatePageController")
@RequestMapping("/page/freightTemplate")
public class FreightTemplatePageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FeignShopCategoryService feignShopCategoryService;

    private SimplePropertyPreFilter simplePropertyPreFilter =  new SimplePropertyPreFilter(CategoryVO.class, "id","name","children");

    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/index")
    public String index(HttpServletRequest request){

        return "freightTemplate/index";
    }


    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/add")
    public String add(HttpServletRequest request){

        return "freightTemplate/add";
    }

}
