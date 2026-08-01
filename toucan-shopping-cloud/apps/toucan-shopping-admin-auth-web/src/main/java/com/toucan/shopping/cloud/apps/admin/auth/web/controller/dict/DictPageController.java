package com.toucan.shopping.cloud.apps.admin.auth.web.controller.dict;

import com.toucan.shopping.modules.admin.auth.vo.DictCategoryVO;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.admin.auth.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.properties.Toucan;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DictPageController extends UIController {

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

    @Autowired
    private DictServiceAPI dictServiceAPI;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
        @RequestMapping(value = "/dict/listPage",method = RequestMethod.GET)
        public String page(HttpServletRequest request)
        {
    
            //初始化选择应用控件
            super.initSelectApp(request,toucan, appServiceAPI);
    
            //初始化工具条按钮、操作按钮
            super.initButtons(request,toucan,"/dict/listPage", functionServiceAPI);
            return "pages/dict/dict/list.html";
        }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
        @RequestMapping(value = "/dict/addPage",method = RequestMethod.GET)
        public String addPage(HttpServletRequest request,@RequestParam Integer dictCategoryId) throws Exception
        {
            DictCategoryVO dictCategoryVO = new DictCategoryVO();
            dictCategoryVO.setId(dictCategoryId);;
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,dictCategoryVO);
            ResultObjectVO resultObjectVO = dictCategoryServiceAPI.findById(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<DictCategoryVO> dictCategoryVOS = resultObjectVO.formatDataList(DictCategoryVO.class);
                if(CollectionUtils.isNotEmpty(dictCategoryVOS)) {
                    DictCategoryVO dictCategoryModel = dictCategoryVOS.get(0);
                    request.setAttribute("dictCategoryModel", dictCategoryModel);
                }else {
                    request.setAttribute("dictCategoryModel", new DictCategoryVO());
                }
            }
    
            return "pages/dict/dict/add.html";
        }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
        @RequestMapping(value = "/dict/editPage/{id}",method = RequestMethod.GET)
        public String editPage(HttpServletRequest request,@PathVariable Long id)
        {
            try {
                super.initSelectApp(request,toucan, appServiceAPI);
    
                DictVO dictVO = new DictVO();
                dictVO.setId(id);
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, dictVO);
                ResultObjectVO resultObjectVO = dictServiceAPI.findById(requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    List<DictVO> dictVOS = resultObjectVO.formatDataList(DictVO.class);
                    if(!CollectionUtils.isEmpty(dictVOS))
                    {
                        dictVO = dictVOS.get(0);
                        DictCategoryVO dictCategory = new DictCategoryVO();
                        dictCategory.setId(dictVO.getCategoryId());
                        requestJsonVO = RequestJsonVOGenerator.generator(appCode, dictCategory);
                        resultObjectVO = dictCategoryServiceAPI.findById(requestJsonVO);
                        if(resultObjectVO.isSuccess())
                        {
                            List<DictCategoryVO> dictCategoryVOS = resultObjectVO.formatDataList(DictCategoryVO.class);
                            if(!CollectionUtils.isEmpty(dictCategoryVOS))
                            {
                                dictCategory = dictCategoryVOS.get(0);
                                dictVO.setCategoryName(dictCategory.getName());
                            }
                        }
                        request.setAttribute("model",dictVO);
                    }
    
                }
            }catch(Exception e)
            {
                logger.warn(e.getMessage(),e);
            }
            return "pages/dict/dict/edit.html";
        }

}
