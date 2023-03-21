package com.toucan.shopping.cloud.apps.web.controller.product;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSpuService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.product.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/product")
public class ProductApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignProductSkuService feignProductSkuService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignProductSpuService feignProductSpuService;


    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    public ResultObjectVO detail(@RequestBody ProductSkuVO productSkuVO)
    {
        ResultObjectVO retObject = new ResultObjectVO();
        try {
            ProductSkuVO queryShopProductSku = new ProductSkuVO();
            queryShopProductSku.setId(productSkuVO.getId());;
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShopProductSku);
            ResultObjectVO resultObjectVO = feignProductSkuService.queryByIdForFront(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    productSkuVO = resultObjectVO.formatData(ProductSkuVO.class);

                    if(productSkuVO.getMainPhotoFilePath()!=null) {
                        productSkuVO.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix()+productSkuVO.getMainPhotoFilePath());
                    }

                    if(org.apache.commons.collections.CollectionUtils.isNotEmpty(productSkuVO.getPreviewPhotoPaths())) {
                        productSkuVO.setHttpPreviewPhotoPaths(new LinkedList<>());
                        for(String previewPhotoPath:productSkuVO.getPreviewPhotoPaths())
                        {
                            productSkuVO.getHttpPreviewPhotoPaths().add(imageUploadService.getImageHttpPrefix()+previewPhotoPath);
                        }
                    }

                    if(productSkuVO.getShopProductDescriptionVO()!=null) {
                        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(productSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs()))
                        {
                            for(ShopProductDescriptionImgVO shopProductDescriptionImgVO:productSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs()) {
                                shopProductDescriptionImgVO.setHttpFilePath(imageUploadService.getImageHttpPrefix()+shopProductDescriptionImgVO.getFilePath());
                            }
                        }
                    }
                    if(productSkuVO.getProductPreviewPath()!=null) {
                        productSkuVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix()+productSkuVO.getProductPreviewPath());
                    }

                    setAttributeDisabled(productSkuVO);

                    retObject.setData(productSkuVO);
                }
            }
        }catch(Exception e)
        {
            retObject.setMsg("查询失败,请稍后重试");
            retObject.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return retObject;
    }

    /**
     * 设置属性禁用状态
     * @param productSkuVO
     */
    private void setAttributeDisabled(ProductSkuVO productSkuVO)
    {
        //将全部属性转换成对象
        Map<String, JSONArray> allAttributeMap = JSONObject.parseObject(productSkuVO.getProductAttributes(),Map.class);
        if(allAttributeMap.size()>0) {
            List<ProductSkuBuyStatusVO> skuBuyStatusVOS = productSkuVO.getSkuBuyStatusList();
            Object[] attributeKeys = allAttributeMap.keySet().toArray();
            //构造出二维数组,在每一个元素中会有是否启用属性,返回到前端进行判断
            int elementSize = allAttributeMap.get(attributeKeys[0]).size();
            AttributeValueStatusVO[][] attributeValueStatusVOS =
                    new AttributeValueStatusVO[attributeKeys.length][elementSize];
            if (CollectionUtils.isNotEmpty(skuBuyStatusVOS)) {
                //填充属性二维数组
                for(int i=0;i<attributeKeys.length;i++)
                {
                    for(int j=0;j<elementSize;j++)
                    {
                        AttributeValueStatusVO attributeValueStatusVO = new AttributeValueStatusVO();
                        attributeValueStatusVO.setStatus(1);
                        attributeValueStatusVO.setValue(String.valueOf(allAttributeMap.get(attributeKeys[i]).get(j)));
                        attributeValueStatusVOS[i][j] = attributeValueStatusVO;
                    }
                }

                boolean isDisabled = false;
                //设置当前页属性二维数组启用/禁用状态
                for (int i = 0; i < skuBuyStatusVOS.size(); i++) {
                    ProductSkuBuyStatusVO productSkuBuyStatusVO = skuBuyStatusVOS.get(i);
                    isDisabled = false;
                    String parentAttribute = productSkuBuyStatusVO.getAttributeValueGroup().substring(0, productSkuBuyStatusVO.getAttributeValueGroup().lastIndexOf("_"));
                    String currentSkuParentAttribute = productSkuVO.getAttributeValueGroup().substring(0, productSkuVO.getAttributeValueGroup().lastIndexOf("_"));

                    //判断是当前商品页的属性路径
                    if (parentAttribute.equals(currentSkuParentAttribute)) {
                        if (productSkuBuyStatusVO.getAttributeValueGroup().indexOf("_") != -1)  //多组属性
                        {
                            if (productSkuBuyStatusVO.getStatus() != null && productSkuBuyStatusVO.getStatus().intValue() == 0) {
                                isDisabled = true;
                            }
                            if (productSkuBuyStatusVO.getStockNum() != null && productSkuBuyStatusVO.getStockNum().intValue() <= 0) {
                                isDisabled = true;
                            }
                            if (isDisabled) {
                                String[] attributeValueGroup = productSkuBuyStatusVO.getAttributeValueGroup().split("_");
                                int attributeValueGroupLength = attributeValueGroup.length;
                                for (int j = 0; j < elementSize; j++) {
                                    AttributeValueStatusVO valueStatusVO = attributeValueStatusVOS[attributeValueGroupLength - 1][j];
                                    if (valueStatusVO.getValue().equals(attributeValueGroup[attributeValueGroupLength - 1])) {
                                        valueStatusVO.setStatus(0);
                                    }
                                }
                            }
                        } else { //单组属性
                            for (int s = 0; s < attributeKeys.length; s++) {
                                for (int j = 0; j < elementSize; j++) {
                                    AttributeValueStatusVO valueStatusVO = attributeValueStatusVOS[s][j];
                                    if (valueStatusVO.getValue().equals(productSkuBuyStatusVO.getAttributeValueGroup())) {
                                        if (productSkuBuyStatusVO.getStatus() != null && productSkuBuyStatusVO.getStatus().intValue() == 0) {
                                            valueStatusVO.setStatus(0);
                                        }
                                        if (productSkuBuyStatusVO.getStockNum() != null && productSkuBuyStatusVO.getStockNum().intValue() <= 0) {
                                            valueStatusVO.setStatus(0);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                //设置倒数第二个属性选择项及以上的启用/禁用状态
                for (int i = 0; i < skuBuyStatusVOS.size(); i++) {
                    ProductSkuBuyStatusVO productSkuBuyStatusVO = skuBuyStatusVOS.get(i);
                    String parentAttribute = productSkuBuyStatusVO.getAttributeValueGroup().substring(0, productSkuBuyStatusVO.getAttributeValueGroup().lastIndexOf("_"));
                    String currentSkuParentAttribute = productSkuVO.getAttributeValueGroup().substring(0, productSkuVO.getAttributeValueGroup().lastIndexOf("_"));

                    //判断不是当前商品页的属性路径
                    if (!parentAttribute.equals(currentSkuParentAttribute)) {
                        if (productSkuBuyStatusVO.getStatus() != null && productSkuBuyStatusVO.getStatus().intValue() == 0) {
//                        valueStatusVO.setStatus(0);
                        }
                        if (productSkuBuyStatusVO.getStockNum() != null && productSkuBuyStatusVO.getStockNum().intValue() <= 0) {
//                        valueStatusVO.setStatus(0);
                        }
                    }
                }

            }

            productSkuVO.setAttributeValueStatusVOS(attributeValueStatusVOS);
            productSkuVO.setSkuBuyStatusList(null);
        }
    }

    @RequestMapping(value = "/preview",method = RequestMethod.POST)
    public ResultObjectVO preview(@RequestBody ProductSkuVO shopProductSkuVO)
    {
        ResultObjectVO retObject = new ResultObjectVO();
        try {
            ProductSkuVO queryShopProductSku = new ProductSkuVO();
            queryShopProductSku.setId(shopProductSkuVO.getId());;
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShopProductSku);
            ResultObjectVO resultObjectVO = feignProductSkuService.queryByIdForFrontPreview(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    shopProductSkuVO = resultObjectVO.formatData(ProductSkuVO.class);

                    if(shopProductSkuVO.getMainPhotoFilePath()!=null) {
                        shopProductSkuVO.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix()+shopProductSkuVO.getMainPhotoFilePath());
                    }

                    if(org.apache.commons.collections.CollectionUtils.isNotEmpty(shopProductSkuVO.getPreviewPhotoPaths())) {
                        shopProductSkuVO.setHttpPreviewPhotoPaths(new LinkedList<>());
                        for(String previewPhotoPath:shopProductSkuVO.getPreviewPhotoPaths())
                        {
                            shopProductSkuVO.getHttpPreviewPhotoPaths().add(imageUploadService.getImageHttpPrefix()+previewPhotoPath);
                        }
                    }

                    if(shopProductSkuVO.getShopProductDescriptionVO()!=null) {
                        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(shopProductSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs()))
                        {
                            for(ShopProductDescriptionImgVO shopProductDescriptionImgVO:shopProductSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs()) {
                                shopProductDescriptionImgVO.setHttpFilePath(imageUploadService.getImageHttpPrefix()+shopProductDescriptionImgVO.getFilePath());
                            }
                        }
                    }
                    if(shopProductSkuVO.getProductPreviewPath()!=null) {
                        shopProductSkuVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix()+shopProductSkuVO.getProductPreviewPath());
                    }


                    retObject.setData(shopProductSkuVO);
                }
            }
        }catch(Exception e)
        {
            retObject.setMsg("查询失败,请稍后重试");
            retObject.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return retObject;
    }

    /**
     * 根据商品主表ID查询1个SKU
     * @param shopProductVO
     * @return
     */
    @RequestMapping(value = "/detail/pid",method = RequestMethod.POST)
    public ResultObjectVO detailByProductId(@RequestBody ShopProductVO shopProductVO)
    {
        ResultObjectVO retObject = new ResultObjectVO();
        try {
            ShopProductVO queryShopProduct = new ShopProductVO();
            queryShopProduct.setId(shopProductVO.getId());;
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShopProduct);
            ResultObjectVO resultObjectVO = feignProductSkuService.queryOneByShopProductIdForFront(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    ProductSkuVO productSkuVO = resultObjectVO.formatData(ProductSkuVO.class);

                    if(productSkuVO.getMainPhotoFilePath()!=null) {
                        productSkuVO.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix()+productSkuVO.getMainPhotoFilePath());
                    }

                    if(org.apache.commons.collections.CollectionUtils.isNotEmpty(productSkuVO.getPreviewPhotoPaths())) {
                        productSkuVO.setHttpPreviewPhotoPaths(new LinkedList<>());
                        for(String previewPhotoPath:productSkuVO.getPreviewPhotoPaths())
                        {
                            productSkuVO.getHttpPreviewPhotoPaths().add(imageUploadService.getImageHttpPrefix()+previewPhotoPath);
                        }
                    }

                    if(productSkuVO.getShopProductDescriptionVO()!=null) {
                        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(productSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs()))
                        {
                            for(ShopProductDescriptionImgVO shopProductDescriptionImgVO:productSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs()) {
                                shopProductDescriptionImgVO.setHttpFilePath(imageUploadService.getImageHttpPrefix()+shopProductDescriptionImgVO.getFilePath());
                            }
                        }
                    }
                    if(productSkuVO.getProductPreviewPath()!=null) {
                        productSkuVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix()+productSkuVO.getProductPreviewPath());
                    }

                    setAttributeDisabled(productSkuVO);

                    retObject.setData(productSkuVO);
                }
            }
        }catch(Exception e)
        {
            retObject.setMsg("查询失败,请稍后重试");
            retObject.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return retObject;
    }



    /**
     * 根据商品主表ID查询1个预览的SKU
     * @param shopProductVO
     * @return
     */
    @RequestMapping(value = "/preview/pid",method = RequestMethod.POST)
    public ResultObjectVO previewByProductId(@RequestBody ShopProductVO shopProductVO)
    {
        ResultObjectVO retObject = new ResultObjectVO();
        try {
            ShopProductVO queryShopProduct = new ShopProductVO();
            queryShopProduct.setId(shopProductVO.getId());;
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShopProduct);
            ResultObjectVO resultObjectVO = feignProductSkuService.queryOneByShopProductIdForFrontPreview(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    ProductSkuVO productSkuVO = resultObjectVO.formatData(ProductSkuVO.class);

                    if(productSkuVO.getMainPhotoFilePath()!=null) {
                        productSkuVO.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix()+productSkuVO.getMainPhotoFilePath());
                    }

                    if(org.apache.commons.collections.CollectionUtils.isNotEmpty(productSkuVO.getPreviewPhotoPaths())) {
                        productSkuVO.setHttpPreviewPhotoPaths(new LinkedList<>());
                        for(String previewPhotoPath:productSkuVO.getPreviewPhotoPaths())
                        {
                            productSkuVO.getHttpPreviewPhotoPaths().add(imageUploadService.getImageHttpPrefix()+previewPhotoPath);
                        }
                    }

                    if(productSkuVO.getShopProductDescriptionVO()!=null) {
                        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(productSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs()))
                        {
                            for(ShopProductDescriptionImgVO shopProductDescriptionImgVO:productSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs()) {
                                shopProductDescriptionImgVO.setHttpFilePath(imageUploadService.getImageHttpPrefix()+shopProductDescriptionImgVO.getFilePath());
                            }
                        }
                    }
                    if(productSkuVO.getProductPreviewPath()!=null) {
                        productSkuVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix()+productSkuVO.getProductPreviewPath());
                    }


                    retObject.setData(productSkuVO);
                }
            }
        }catch(Exception e)
        {
            retObject.setMsg("查询失败,请稍后重试");
            retObject.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return retObject;
    }


    /**
     * 根据商品主表ID查询1个预览的SKU
     * @param shopProductVO
     * @return
     */
    @RequestMapping(value = "/spu/info",method = RequestMethod.POST)
    public ResultObjectVO querySpuAttributeList(@RequestBody ShopProductVO shopProductVO)
    {
        ResultObjectVO retObject = new ResultObjectVO();
        try {
            if(shopProductVO.getProductId()==null)
            {
                retObject.setMsg("查询失败,没有找到商品ID");
                retObject.setCode(ResultObjectVO.FAILD);
            }

            ProductSpuVO queryProductSpu = new ProductSpuVO();
            queryProductSpu.setId(shopProductVO.getProductId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryProductSpu);
            retObject = feignProductSpuService.findById(requestJsonVO);

        }catch(Exception e)
        {
            retObject.setMsg("查询失败,请稍后重试");
            retObject.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return retObject;
    }





}
