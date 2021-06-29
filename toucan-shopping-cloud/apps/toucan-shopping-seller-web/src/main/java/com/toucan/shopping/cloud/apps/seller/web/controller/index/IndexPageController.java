package com.toucan.shopping.cloud.apps.seller.web.controller.index;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 首页控制器
 */
@Controller("pageIndexController")
public class IndexPageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @RequestMapping("/index")
    public String index(HttpServletRequest request)
    {
        return "index";
    }


    @RequestMapping("/")
    public String defaultIndex(HttpServletRequest request)
    {
        return index(request);
    }

}
