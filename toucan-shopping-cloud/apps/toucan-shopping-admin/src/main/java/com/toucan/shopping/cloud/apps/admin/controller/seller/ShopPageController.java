package com.toucan.shopping.cloud.apps.admin.controller.seller;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.seller.api.SellerShopServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *  卖家店铺管理 - 页面控制器
 */
@Controller
public class ShopPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private SellerShopServiceAPI sellerShopService;

    @Autowired
    private ImageUploadService imageUploadService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/shop/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/shop/listPage", functionServiceAPI);
        return "pages/seller/shop/list.html";
    }


    /**
     * 编辑店铺
     * @param request
     * @param id
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/shop/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            SellerShop entity = new SellerShop();
            entity.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            ResultObjectVO resultObjectVO = sellerShopService.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    List<SellerShop> entitys = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),SellerShop.class);
                    if(!CollectionUtils.isEmpty(entitys))
                    {
                        SellerShopVO sellerShopVO = new SellerShopVO();
                        BeanUtils.copyProperties(sellerShopVO,entitys.get(0));
                        if(StringUtils.isNotEmpty(sellerShopVO.getLogo()))
                        {
                            sellerShopVO.setHttpLogo(imageUploadService.getImageHttpPrefix()+"/"+sellerShopVO.getLogo());
                        }
                        request.setAttribute("model",sellerShopVO);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/seller/shop/edit.html";
    }

    /**
     * 查看店铺
     * @param request
     * @param id
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/shop/detailPage/{id}",method = RequestMethod.GET)
    public String detailPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            SellerShop entity = new SellerShop();
            entity.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            ResultObjectVO resultObjectVO = sellerShopService.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    List<SellerShop> entitys = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),SellerShop.class);
                    if(!CollectionUtils.isEmpty(entitys))
                    {
                        SellerShopVO sellerShopVO = new SellerShopVO();
                        BeanUtils.copyProperties(sellerShopVO,entitys.get(0));
                        if(StringUtils.isNotEmpty(sellerShopVO.getLogo()))
                        {
                            sellerShopVO.setHttpLogo(imageUploadService.getImageHttpPrefix()+"/"+sellerShopVO.getLogo());
                        }
                        request.setAttribute("model",sellerShopVO);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/seller/shop/detail.html";
    }

}
