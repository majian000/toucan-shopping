package com.toucan.shopping.cloud.apps.admin.controller.dict;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.admin.auth.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.properties.Toucan;
import jakarta.servlet.http.HttpServletRequest;
import com.toucan.shopping.modules.admin.auth.entity.*;
import com.toucan.shopping.modules.admin.auth.vo.*;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DictCategoryPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private AppServiceAPI appServiceAPI;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;


    @Autowired
    private DictCategoryServiceAPI dictCategoryServiceAPI;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
        @RequestMapping(value = "/dictCategory/listPage",method = RequestMethod.GET)
        public String page(HttpServletRequest request)
        {
    
            //初始化选择应用控件
            super.initSelectApp(request,toucan, appServiceAPI);
    
            //初始化工具条按钮、操作按钮
            super.initButtons(request,toucan,"/dictCategory/listPage", functionServiceAPI);
            return "pages/dict/dictCategory/list.html";
        }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
        @RequestMapping(value = "/dictCategory/addPage",method = RequestMethod.GET)
        public String addPage(HttpServletRequest request)
        {
            super.initSelectApp(request,toucan, appServiceAPI);
    
            return "pages/dict/dictCategory/add.html";
        }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
        @RequestMapping(value = "/dictCategory/editPage/{id}",method = RequestMethod.GET)
        public String editPage(HttpServletRequest request,@PathVariable Integer id)
        {
            try {
                super.initSelectApp(request,toucan, appServiceAPI);
    
                DictCategoryVO dictCategory = new DictCategoryVO();
                dictCategory.setId(id);
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, dictCategory);
                ResultObjectVO resultObjectVO = dictCategoryServiceAPI.findById(requestJsonVO);
                if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
                {
                    if(resultObjectVO.getData()!=null) {
    
                        List<DictCategoryVO> dictCategoryVOS = resultObjectVO.formatDataList(DictCategoryVO.class);
                        if(!CollectionUtils.isEmpty(dictCategoryVOS))
                        {
                            dictCategory = dictCategoryVOS.get(0);
                            request.setAttribute("model",dictCategory);
                        }
                    }
    
                }
            }catch(Exception e)
            {
                logger.warn(e.getMessage(),e);
            }
            return "pages/dict/dictCategory/edit.html";
        }

}
