package com.toucan.shopping.cloud.apps.admin.controller.freightTemplate;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductApproveService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignFreightTemplateService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.persistence.event.entity.EventPublish;
import com.toucan.shopping.modules.common.persistence.event.enums.EventPublishTypeEnum;
import com.toucan.shopping.modules.common.persistence.event.service.EventPublishService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.message.vo.MessageVO;
import com.toucan.shopping.modules.product.page.ProductSkuPageInfo;
import com.toucan.shopping.modules.product.page.ShopProductPageInfo;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.seller.page.FreightTemplatePageInfo;
import com.toucan.shopping.modules.seller.util.FreightTemplateUtils;
import com.toucan.shopping.modules.seller.vo.FreightTemplateVO;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
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
 * 运费模板
 * @author majian
 */
@Controller
@RequestMapping("/freightTemplate")
public class FreightTemplateController extends UIController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignFreightTemplateService feignFreightTemplateService;


    @Autowired
    private FeignShopProductApproveService feignShopProductApproveService;

    @Autowired
    private FeignShopProductService feignShopProductService;

    @Autowired
    private IdGenerator idGenerator;



    /**
     * 查询详情
     * @param freightTemplateVO
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO detail(HttpServletRequest request,@RequestBody FreightTemplateVO freightTemplateVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(freightTemplateVO.getId()==null)
            {
                resultObjectVO.setCode(TableVO.FAILD);
                resultObjectVO.setMsg("ID不能为空");
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),freightTemplateVO);
            resultObjectVO = feignFreightTemplateService.findById(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("操作失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/freightTemplate/listPage",feignFunctionService);
        return "pages/seller/freightTemplate/list.html";
    }


    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest httpServletRequest,FreightTemplatePageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try{
            if(pageInfo==null)
            {
                pageInfo = new FreightTemplatePageInfo();
            }

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            ResultObjectVO resultObjectVO = feignFreightTemplateService.queryListPage(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    Map<String, Object> resultObjectDataMap = (Map<String, Object>) resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total") != null ? resultObjectDataMap.get("total") : "0")));
                    List<FreightTemplateVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), FreightTemplateVO.class);
                    tableVO.setData((List)list);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            tableVO.setCode(ResultObjectVO.FAILD);
            tableVO.setMsg("查询失败,请稍后重试");
        }

        return tableVO;
    }


    /**
     * 根据ID删除
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value="/delete/{id}",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO deleteById(HttpServletRequest request,@PathVariable Long id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(id==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("ID不能为空");
            return resultObjectVO;
        }
        try {
            FreightTemplateVO freightTemplateVO = new FreightTemplateVO();
            freightTemplateVO.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),freightTemplateVO);
            resultObjectVO = feignFreightTemplateService.findById(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                FreightTemplateVO freightTemplateRetVO = resultObjectVO.formatData(FreightTemplateVO.class);
                ShopProductApproveVO shopProductApproveVO = new ShopProductApproveVO();
                shopProductApproveVO.setFreightTemplateId(id);
                shopProductApproveVO.setShopId(freightTemplateRetVO.getShopId());
                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopProductApproveVO);
                resultObjectVO = feignShopProductApproveService.findOneUnderReviewByFreightTemplateId(requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    if(resultObjectVO.getData()!=null) {
                        shopProductApproveVO = resultObjectVO.formatData(ShopProductApproveVO.class);
                        if (shopProductApproveVO != null) {

                            resultObjectVO.setCode(ResultObjectVO.FAILD);
                            resultObjectVO.setMsg("无法删除该模板,因为\""+shopProductApproveVO.getName()+"\"正在发布中,您可以删除该商品后重试");
                            return resultObjectVO;
                        }
                    }
                }



                ShopProductVO shopProductVO = new ShopProductVO();
                shopProductVO.setFreightTemplateId(id);
                shopProductVO.setShopId(freightTemplateRetVO.getShopId());
                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopProductVO);
                resultObjectVO = feignShopProductService.queryOneByFreightTemplateId(requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    if(resultObjectVO.getData()!=null) {
                        shopProductVO = resultObjectVO.formatData(ShopProductVO.class);
                        if (shopProductApproveVO != null) {

                            resultObjectVO.setCode(ResultObjectVO.FAILD);
                            resultObjectVO.setMsg("无法删除该模板,因为已关联商品\""+shopProductVO.getName()+"\",您可以将该商品运费模板更换成其他后重试");
                            return resultObjectVO;
                        }
                    }
                }

                freightTemplateVO = new FreightTemplateVO();
                freightTemplateVO.setId(id);
                freightTemplateVO.setUserMainId(freightTemplateRetVO.getUserMainId());
                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),freightTemplateVO);
                resultObjectVO = feignFreightTemplateService.deleteById(FreightTemplateUtils.getDeleteSignHeader(String.valueOf(freightTemplateRetVO.getUserMainId())),requestJsonVO);

            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 查看运费模板
     * @param request
     * @param id
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/detailPage/{id}",method = RequestMethod.GET)
    public String detailPage(HttpServletRequest request,@PathVariable Long id)
    {
        request.setAttribute("freightTemplateId",id);
        return "pages/seller/freightTemplate/detail.html";
    }

}

