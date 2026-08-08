package com.toucan.shopping.cloud.apps.admin.controller.product.attribute;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.product.api.AttributeValueServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.vo.AttributeValueVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 商品属性值管理页面控制器
 */
@Controller
public class AttributeValuePageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private AttributeValueServiceAPI attributeValueService;



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/attributeValue/listPage/{attributeKeyId}",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request,@PathVariable Long attributeKeyId)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/product/attribute/attributeValue/listPage", functionServiceAPI);
        request.setAttribute("attributeKeyId",attributeKeyId);
        return "pages/product/attribute/attributeValue/list.html";
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/attributeValue/addPage/{attributeKeyId}",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request,@PathVariable Long attributeKeyId)
    {
        request.setAttribute("attributeKeyId",attributeKeyId);
        return "pages/product/attribute/attributeValue/add.html";
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/attributeValue/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            AttributeValueVO attributeValueVO = new AttributeValueVO();
            attributeValueVO.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, attributeValueVO);
            ResultObjectVO resultObjectVO = attributeValueService.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    List<AttributeValueVO> attributeValueVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),AttributeValueVO.class);
                    if(!CollectionUtils.isEmpty(attributeValueVOS))
                    {
                        attributeValueVO = attributeValueVOS.get(0);

                        request.setAttribute("model",attributeValueVO);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/product/attribute/attributeValue/edit.html";
    }




}