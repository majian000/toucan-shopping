package com.toucan.shopping.cloud.apps.admin.controller.user;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.message.api.feign.service.FeignMessageUserService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserCollectProductService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserTrueNameApproveService;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.persistence.event.entity.EventPublish;
import com.toucan.shopping.modules.common.persistence.event.enums.EventPublishTypeEnum;
import com.toucan.shopping.modules.common.persistence.event.service.EventPublishService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.message.constant.MessageContentTypeConstant;
import com.toucan.shopping.modules.message.vo.MessageVO;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.user.page.UserCollectProductPageInfo;
import com.toucan.shopping.modules.user.page.UserTrueNameApprovePageInfo;
import com.toucan.shopping.modules.user.vo.UserCollectProductVO;
import com.toucan.shopping.modules.user.vo.UserTrueNameApproveVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 用户收藏商品管理
 */
@Controller
@RequestMapping("/user/collect/product")
public class UserCollectProductController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private FeignFunctionService feignFunctionService;

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignUserCollectProductService feignUserCollectProductService;

    @Autowired
    private FeignProductSkuService feignProductSkuService;

    @Autowired
    private ImageUploadService imageUploadService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/user/collect/product/listPage",feignFunctionService);
        return "pages/user/userCollectProduct/list.html";
    }




    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, UserCollectProductPageInfo pageInfo)
    {
        TableVO<UserCollectProductVO> tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = feignUserCollectProductService.queryListPage(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null)
                {
                    pageInfo = resultObjectVO.formatData(UserCollectProductPageInfo.class);
                    tableVO.setCount(pageInfo.getTotal());
                    List<ProductSkuVO> productSkus = new LinkedList<ProductSkuVO>();
                    for (UserCollectProductVO userCollectProductVO : pageInfo.getList()) {
                        ProductSkuVO productSkuVO = new ProductSkuVO();
                        productSkuVO.setId(userCollectProductVO.getProductSkuId());
                        productSkus.add(productSkuVO);
                    }
                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),productSkus);
                    ResultObjectVO productResultObjectVO = feignProductSkuService.queryByIdList(requestJsonVO.sign(),requestJsonVO);
                    if(productResultObjectVO.isSuccess())
                    {
                        List<ProductSku> productSkuList = productResultObjectVO.formatDataList(ProductSku.class);
                        if(CollectionUtils.isNotEmpty(productSkuList))
                        {
                            for(ProductSku productSku:productSkuList)
                            {
                                for(UserCollectProductVO userCollectProductVO : pageInfo.getList())
                                {
                                    if(productSku.getId().longValue()==userCollectProductVO.getProductSkuId().longValue()) {
                                        userCollectProductVO.setProductSkuName(productSku.getName());
                                        if(productSku.getStatus().intValue()==0)
                                        {
                                            userCollectProductVO.setProductSkuName(userCollectProductVO.getProductSkuName()+" 已下架");
                                        }
                                        if(productSku.getStockNum().longValue()<=0)
                                        {
                                            userCollectProductVO.setProductSkuName(userCollectProductVO.getProductSkuName()+" 已售罄");
                                        }
                                        userCollectProductVO.setProductPrice(productSku.getPrice());
                                        userCollectProductVO.setHttpProductImgPath(imageUploadService.getImageHttpPrefix()+productSku.getProductPreviewPath());
                                        continue;
                                    }
                                }
                            }
                        }

                    }
                    if(tableVO.getCount()>0) {
                        tableVO.setData(pageInfo.getList());
                    }
                }
            }
        }catch(Exception e)
        {
            tableVO.setMsg("请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.error(e.getMessage(),e);
        }
        return tableVO;
    }



}

