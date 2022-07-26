package com.toucan.shopping.cloud.apps.web.controller.user.buyCar;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignFreightTemplateService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserBuyCarService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.seller.vo.FreightTemplateAreaRuleVO;
import com.toucan.shopping.modules.seller.vo.FreightTemplateVO;
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;
import com.toucan.shopping.modules.user.vo.freightTemplate.UBCIFreightTemplateAreaRuleVO;
import com.toucan.shopping.modules.user.vo.freightTemplate.UBCIFreightTemplateDefaultRuleVO;
import com.toucan.shopping.modules.user.vo.freightTemplate.UBCIFreightTemplateVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * 用户购物车
 */
@Controller("buyCarApiController")
@RequestMapping("/api/user/buyCar")
public class UserBuyCarApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignUserBuyCarService feignUserBuyCarService;

    @Autowired
    private FeignProductSkuService feignProductSkuService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignFreightTemplateService feignFreightTemplateService;

    @Autowired
    private Toucan toucan;

    @UserAuth
    @RequestMapping("/preview/info")
    @ResponseBody
    public ResultObjectVO previewInfo(HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId="-1";
        try{
            //从请求头中拿到uid
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            UserBuyCarItemVO userBuyCarVO = new UserBuyCarItemVO();
            userBuyCarVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),userBuyCarVO);
            ResultObjectVO userBuyCarResultObjectVO  = feignUserBuyCarService.listByUserMainId(requestJsonVO);
            if(userBuyCarResultObjectVO.isSuccess())
            {
                List<UserBuyCarItemVO> userBuyCarVOList = userBuyCarResultObjectVO.formatDataList(UserBuyCarItemVO.class);
                List<ProductSkuVO> productSkus = new LinkedList<ProductSkuVO>();
                for(UserBuyCarItemVO ubc:userBuyCarVOList)
                {
                    ProductSkuVO productSkuVO = new ProductSkuVO();
                    productSkuVO.setId(ubc.getShopProductSkuId());
                    productSkus.add(productSkuVO);
                }

                if(CollectionUtils.isNotEmpty(productSkus)) {
                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), productSkus);

                    ResultObjectVO ResultProductSkuObjectVO = feignProductSkuService.queryByIdList(requestJsonVO.sign(), requestJsonVO);
                    if(!ResultProductSkuObjectVO.isSuccess())
                    {
                        for(UserBuyCarItemVO ubc:userBuyCarVOList) {
                            ubc.setProductSkuName("商品已下架");
                            ubc.setAllowedBuy(false);
                            ubc.setShopId(-1L);
                            ubc.setAttributePreview("");
                            ubc.setProductPrice(new BigDecimal(0.0D));
                            ubc.setFreightTemplateId(-1L);
                            ubc.setNoAllowedBuyStatus((short) 1);
                        }
                    }else{
                        List<ProductSku> productSkuList = ResultProductSkuObjectVO.formatDataList(ProductSku.class);
                        if(CollectionUtils.isEmpty(productSkuList))
                        {
                            for(UserBuyCarItemVO ubc:userBuyCarVOList) {
                                ubc.setProductSkuName("商品");
                                ubc.setNoAllowedBuyDesc("已下架");
                                ubc.setAllowedBuy(false);
                                ubc.setShopId(-1L);
                                ubc.setAttributePreview("");
                                ubc.setProductPrice(new BigDecimal(0.0D));
                                ubc.setFreightTemplateId(-1L);
                                ubc.setNoAllowedBuyStatus((short) 1);
                            }
                        }else{
                            boolean isFind=false;
                            for(UserBuyCarItemVO ubc:userBuyCarVOList)
                            {
                                isFind = false;
                                for(ProductSku productSku:productSkuList)
                                {
                                    if(productSku.getId().longValue()==ubc.getShopProductSkuId().longValue()) {

                                        isFind = true;
                                        ubc.setProductSkuName(productSku.getName());
                                        if(productSku.getStatus().intValue()==0)
                                        {
                                            ubc.setProductSkuName(ubc.getProductSkuName());
                                            ubc.setNoAllowedBuyDesc("已下架");
                                            ubc.setAllowedBuy(false);
                                            ubc.setNoAllowedBuyStatus((short)1);
                                        }
                                        if(productSku.getStockNum().longValue()<=0)
                                        {
                                            ubc.setProductSkuName(ubc.getProductSkuName());
                                            ubc.setNoAllowedBuyDesc("已售罄");
                                            ubc.setAllowedBuy(false);
                                            ubc.setNoAllowedBuyStatus((short)2);
                                        }
                                        ubc.setProductPrice(productSku.getPrice());
                                        ubc.setHttpProductImgPath(imageUploadService.getImageHttpPrefix()+productSku.getProductPreviewPath());
                                        continue;
                                    }
                                }
                                if(!isFind)
                                {
                                    ubc.setProductSkuName("商品");
                                    ubc.setNoAllowedBuyDesc("已下架");
                                    ubc.setAllowedBuy(false);
                                    ubc.setShopId(-1L);
                                    ubc.setProductPrice(new BigDecimal(0.0D));
                                    ubc.setAttributePreview("");
                                    ubc.setFreightTemplateId(-1L);
                                    ubc.setNoAllowedBuyStatus((short) 1);
                                }
                            }
                        }
                    }
                }else{
                    for(UserBuyCarItemVO ubc:userBuyCarVOList) {
                        ubc.setProductSkuName("商品");
                        ubc.setNoAllowedBuyDesc("已下架");
                        ubc.setAllowedBuy(false);
                        ubc.setShopId(-1L);
                        ubc.setAttributePreview("");
                        ubc.setProductPrice(new BigDecimal(0.0D));
                        ubc.setFreightTemplateId(-1L);
                        ubc.setNoAllowedBuyStatus((short) 1);
                    }
                }
                resultObjectVO.setData(userBuyCarVOList);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    @UserAuth
    @RequestMapping("/list")
    @ResponseBody
    public ResultObjectVO list(HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId="-1";
        try{
            //从请求头中拿到uid
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            UserBuyCarItemVO userBuyCarVO = new UserBuyCarItemVO();
            userBuyCarVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),userBuyCarVO);
            ResultObjectVO userBuyCarResultObjectVO  = feignUserBuyCarService.listByUserMainId(requestJsonVO);
            if(userBuyCarResultObjectVO.isSuccess())
            {
                List<UserBuyCarItemVO> userBuyCarVOList = userBuyCarResultObjectVO.formatDataList(UserBuyCarItemVO.class);
                List<ProductSkuVO> productSkus = new LinkedList<ProductSkuVO>();
                for(UserBuyCarItemVO ubc:userBuyCarVOList)
                {
                    ProductSkuVO productSkuVO = new ProductSkuVO();
                    productSkuVO.setId(ubc.getShopProductSkuId());
                    productSkus.add(productSkuVO);
                }

                if(CollectionUtils.isNotEmpty(productSkus)) {
                    //查询商品信息
                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), productSkus);
                    ResultObjectVO ResultProductSkuObjectVO = feignProductSkuService.queryByIdList(requestJsonVO.sign(), requestJsonVO);
                    if(ResultProductSkuObjectVO.isSuccess())
                    {
                        List<Long> freightTemplateIdList = new LinkedList<>();
                        List<ProductSkuVO> productSkuList = ResultProductSkuObjectVO.formatDataList(ProductSkuVO.class);
                        if(CollectionUtils.isEmpty(productSkuList)) {
                            for(UserBuyCarItemVO ubc:userBuyCarVOList) {
                                ubc.setProductSkuName("商品");
                                ubc.setNoAllowedBuyDesc("已下架");
                                ubc.setAllowedBuy(false);
                                ubc.setShopId(-1L);
                                ubc.setAttributePreview("");
                                ubc.setProductPrice(new BigDecimal(0.0D));
                                ubc.setFreightTemplateId(-1L);
                                ubc.setNoAllowedBuyStatus((short) 1);
                            }
                        }else{
                            boolean isFind=false;

                            for (ProductSkuVO productSku : productSkuList) {
                                freightTemplateIdList.add(productSku.getFreightTemplateId());
                            }
                            for(UserBuyCarItemVO ubc:userBuyCarVOList) {
                                isFind = false;
                                for (ProductSkuVO productSku : productSkuList) {
                                    if (productSku.getId().longValue() == ubc.getShopProductSkuId().longValue()) {
                                        isFind = true;
                                        ubc.setProductSkuName(productSku.getName());
                                        if (productSku.getStatus().intValue() == 0) {
                                            ubc.setProductSkuName(ubc.getProductSkuName());
                                            ubc.setNoAllowedBuyDesc("已下架");
                                            ubc.setAllowedBuy(false);
                                            ubc.setNoAllowedBuyStatus((short) 1);
                                        }
                                        if (productSku.getStockNum().longValue() <= 0) {
                                            ubc.setProductSkuName(ubc.getProductSkuName());
                                            ubc.setNoAllowedBuyDesc("已售罄");
                                            ubc.setAllowedBuy(false);
                                            ubc.setNoAllowedBuyStatus((short) 2);
                                        }
                                        ubc.setProductPrice(productSku.getPrice());
                                        ubc.setSuttle(productSku.getSuttle()); //净重
                                        ubc.setRoughWeight(productSku.getRoughWeight()); //毛重
                                        ubc.setHttpProductImgPath(imageUploadService.getImageHttpPrefix() + productSku.getProductPreviewPath());
                                        if (productSku.getFreightTemplateId() != null) {
                                            ubc.setFreightTemplateId(productSku.getFreightTemplateId());
                                        }
                                        ubc.setShopId(productSku.getShopId());

                                        StringBuilder attributePreview = new StringBuilder();
                                        HashMap<String, String> attributeMap = JSONObject.parseObject(productSku.getAttributes(), HashMap.class);
                                        Set<String> attributeKeys = attributeMap.keySet();
                                        for (String attributeKey : attributeKeys) {
                                            String attributeValue = attributeMap.get(attributeKey);
                                            attributePreview.append(attributeKey + ":" + attributeValue);
                                            attributePreview.append(" ");
                                        }
                                        ubc.setAttributePreview(attributePreview.toString());
                                        continue;
                                    }
                                }

                                if(!isFind)
                                {
                                    ubc.setProductSkuName("商品");
                                    ubc.setNoAllowedBuyDesc("已下架");
                                    ubc.setAllowedBuy(false);
                                    ubc.setShopId(-1L);
                                    ubc.setProductPrice(new BigDecimal(0.0D));
                                    ubc.setAttributePreview("");
                                    ubc.setFreightTemplateId(-1L);
                                    ubc.setNoAllowedBuyStatus((short) 1);
                                }
                            }


                            //查询运费模板
                            if(CollectionUtils.isNotEmpty(freightTemplateIdList)) {
                                FreightTemplateVO queryFreightTemplateVO = new FreightTemplateVO();
                                queryFreightTemplateVO.setIdList(freightTemplateIdList);
                                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryFreightTemplateVO);
                                resultObjectVO = feignFreightTemplateService.findByIdList(requestJsonVO);
                                if(!resultObjectVO.isSuccess())
                                {
                                    return resultObjectVO;
                                }
                                if(resultObjectVO.getData()!=null)
                                {
                                    List<UBCIFreightTemplateVO> freightTemplateVOS = resultObjectVO.formatDataList(UBCIFreightTemplateVO.class);
                                    if(CollectionUtils.isNotEmpty(freightTemplateVOS))
                                    {
                                        for(UBCIFreightTemplateVO freightTemplateVO:freightTemplateVOS)
                                        {
                                            for(UserBuyCarItemVO ubc:userBuyCarVOList)
                                            {
                                                if(ubc.getFreightTemplateId()!=null
                                                        &&ubc.getFreightTemplateId().longValue()==freightTemplateVO.getId().longValue())
                                                {
                                                    ubc.setFreightTemplateVO(freightTemplateVO);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //按照店铺排序,相邻商品在一起
                    userBuyCarVOList.sort(Comparator.comparing(UserBuyCarItemVO::getShopId).reversed());
                    //按照运费模板排序,用于合并运送方式
                    userBuyCarVOList.sort(Comparator.comparing(UserBuyCarItemVO::getFreightTemplateId).reversed());
                }

                resultObjectVO.setCode(ResultObjectVO.SUCCESS);
                resultObjectVO.setData(userBuyCarVOList);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

    @UserAuth
    @RequestMapping("/save")
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request, @RequestBody UserBuyCarItemVO userBuyCarVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId="-1";
        try{
            //从请求头中拿到uid
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            userBuyCarVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),userBuyCarVO);
            resultObjectVO  = feignUserBuyCarService.save(requestJsonVO);

            //已存在该商品或者保存成功
            if(resultObjectVO.isSuccess()||resultObjectVO.getCode().intValue()==201)
            {
                List<UserBuyCarItemVO> userBuyCarVOList = resultObjectVO.formatDataList(UserBuyCarItemVO.class);
                List<ProductSkuVO> productSkus = new LinkedList<ProductSkuVO>();
                for(UserBuyCarItemVO ubc:userBuyCarVOList)
                {
                    ProductSkuVO productSkuVO = new ProductSkuVO();
                    productSkuVO.setId(ubc.getShopProductSkuId());
                    productSkus.add(productSkuVO);
                }

                if(CollectionUtils.isNotEmpty(productSkus)) {
                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), productSkus);

                    ResultObjectVO ResultProductSkuObjectVO = feignProductSkuService.queryByIdList(requestJsonVO.sign(), requestJsonVO);
                    if(ResultProductSkuObjectVO.isSuccess())
                    {
                        List<ProductSku> productSkuList = ResultProductSkuObjectVO.formatDataList(ProductSku.class);
                        if(CollectionUtils.isNotEmpty(productSkuList))
                        {
                            for(ProductSku productSku:productSkuList)
                            {
                                for(UserBuyCarItemVO ubc:userBuyCarVOList)
                                {
                                    if(productSku.getId().longValue()==ubc.getShopProductSkuId().longValue()) {
                                        ubc.setProductSkuName(productSku.getName());
                                        if(productSku.getStatus().intValue()==0)
                                        {
                                            ubc.setProductSkuName(ubc.getProductSkuName()+" 已下架");
                                        }
                                        if(productSku.getStockNum().longValue()<=0)
                                        {
                                            ubc.setProductSkuName(ubc.getProductSkuName()+" 已售罄");
                                        }
                                        ubc.setProductPrice(productSku.getPrice());
                                        ubc.setHttpProductImgPath(imageUploadService.getImageHttpPrefix()+productSku.getProductPreviewPath());
                                        continue;
                                    }
                                }
                            }
                        }
                    }
                }
                resultObjectVO.setData(userBuyCarVOList);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





    @UserAuth
    @RequestMapping("/remove")
    @ResponseBody
    public ResultObjectVO remove(HttpServletRequest request, @RequestBody UserBuyCarItemVO userBuyCarVO) {

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId = "-1";
        try {
            //从请求头中拿到uid
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            userBuyCarVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userBuyCarVO);
            resultObjectVO = feignUserBuyCarService.removeBuyCar(requestJsonVO);

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }



    @UserAuth
    @RequestMapping("/clear")
    @ResponseBody
    public ResultObjectVO clear(HttpServletRequest request) {

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId = "-1";
        try {
            UserBuyCarItemVO userBuyCarVO = new UserBuyCarItemVO();
            //从请求头中拿到uid
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            userBuyCarVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userBuyCarVO);
            resultObjectVO = feignUserBuyCarService.clearByUserMainId(requestJsonVO);

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }




    @UserAuth
    @RequestMapping("/updates")
    @ResponseBody
    public ResultObjectVO updates(HttpServletRequest request, @RequestBody List<UserBuyCarItemVO> userBuyCarVos) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId = "-1";
        try {
            if(CollectionUtils.isNotEmpty(userBuyCarVos)) {
                //从请求头中拿到uid
                userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
                for(UserBuyCarItemVO userBuyCarItemVO:userBuyCarVos) {
                    userBuyCarItemVO.setUserMainId(Long.parseLong(userMainId));
                }
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userBuyCarVos);
                resultObjectVO = feignUserBuyCarService.updates(requestJsonVO);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }




    @UserAuth
    @RequestMapping("/update")
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request, @RequestBody UserBuyCarItemVO userBuyCarItemVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId = "-1";
        try {
            //从请求头中拿到uid
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            userBuyCarItemVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userBuyCarItemVO);
            resultObjectVO = feignUserBuyCarService.update(requestJsonVO);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }
}
