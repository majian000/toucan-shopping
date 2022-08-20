package com.toucan.shopping.cloud.apps.web.controller.user.buyCar;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
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
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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

                                        StringBuilder attributePreview = new StringBuilder();
                                        HashMap<String,String> attributeMap = JSONObject.parseObject(productSku.getAttributes(), HashMap.class);
                                        Set<String> attributeKeys = attributeMap.keySet();
                                        for(String attributeKey:attributeKeys)
                                        {
                                            String attributeValue = attributeMap.get(attributeKey);
                                            attributePreview.append(attributeKey+":"+attributeValue);
                                            attributePreview.append(" ");
                                        }
                                        ubc.setAttributePreview(attributePreview.toString());
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

            //已存在改商品或者保存成功
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
}
