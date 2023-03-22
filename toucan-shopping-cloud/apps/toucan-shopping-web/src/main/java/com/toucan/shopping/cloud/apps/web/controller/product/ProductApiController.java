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
            Object[] attributeKeys = allAttributeMap.keySet().toArray();
            int elementSize = allAttributeMap.get(attributeKeys[0]).size();
            String[][] attributeValueArray = new String[attributeKeys.length][elementSize];
            //填充属性二维数组
            for(int i=0;i<attributeKeys.length;i++)
            {
                for(int j=0;j<elementSize;j++)
                {
                    attributeValueArray[i][j] = String.valueOf(allAttributeMap.get(attributeKeys[i]).get(j));
                }
            }
            List<AttributeValueStatusVO> attributeValueStatusVOS = new LinkedList<>();
            int hier= 0; //层数
            for(int p=0;p<attributeValueArray[0].length;p++)
            {
                AttributeValueStatusVO attributeValueStatusVO = new AttributeValueStatusVO();
                attributeValueStatusVO.setStatus(1);
                attributeValueStatusVO.setValue(attributeValueArray[0][p]);
                attributeValueStatusVO.setValuePath(attributeValueArray[0][p]);
                if((hier+1)<attributeValueArray.length)
                {
                    //填充这个属性值树结构
                    fillAttributeValueTree(attributeValueStatusVO,hier+1,attributeValueArray);
                }
                attributeValueStatusVOS.add(attributeValueStatusVO);
            }

            //判断属性节点是否可以点击
            boolean isDisabled;
            List<ProductSkuBuyStatusVO> skuBuyStatusVOS = productSkuVO.getSkuBuyStatusList();
            for (int i = 0; i < skuBuyStatusVOS.size(); i++) {
                ProductSkuBuyStatusVO productSkuBuyStatusVO = skuBuyStatusVOS.get(i);
                isDisabled = false;

                if (productSkuBuyStatusVO.getStatus() != null && productSkuBuyStatusVO.getStatus().intValue() == 0) {
                    isDisabled = true;
                }
                if (productSkuBuyStatusVO.getStockNum() != null && productSkuBuyStatusVO.getStockNum().intValue() <= 0) {
                    isDisabled = true;
                }
                if(isDisabled) {
                    AttributeValueStatusVO attributeValueStatusVO = getAttributeValueTreeNode(productSkuBuyStatusVO.getAttributeValueGroup(), attributeValueStatusVOS);
                    if (attributeValueStatusVO != null) {
                        //设置禁用
                        if (productSkuBuyStatusVO.getStatus() != null && productSkuBuyStatusVO.getStatus().intValue() == 0) {
                            attributeValueStatusVO.setStatus(0);
                            attributeValueStatusVO.setStatusCode(2);
                        }
                        if (productSkuBuyStatusVO.getStockNum() != null && productSkuBuyStatusVO.getStockNum().intValue() <= 0) {
                            attributeValueStatusVO.setStatus(0);
                            attributeValueStatusVO.setStatusCode(1);
                        }
                    }
                }
            }

            //设置禁用数量
            for(int i=0;i<attributeValueStatusVOS.size();i++)
            {
                setAttributeValueTreeDisabledCount(attributeValueStatusVOS.get(i),attributeValueStatusVOS.get(i).getChildren(),attributeValueStatusVOS);
            }

            //设置禁用状态
            for(int i=0;i<attributeValueStatusVOS.size();i++) {
                setAttributeValueTreeDisabledStatus(attributeValueStatusVOS.get(i));
            }

            productSkuVO.setAttributeValueStatusVOS(attributeValueStatusVOS);
            productSkuVO.setSkuBuyStatusList(null);
        }
    }

    /**
     * 拿到属性值树节点
     * @param attributeValueGroup
     * @param attributeValueStatusVOS
     * @return
     */
    public AttributeValueStatusVO getAttributeValueTreeNode(String attributeValueGroup,List<AttributeValueStatusVO> attributeValueStatusVOS)
    {
        AttributeValueStatusVO ret = null;
        if (CollectionUtils.isNotEmpty(attributeValueStatusVOS)&&ret==null) {
            for (AttributeValueStatusVO attributeValueStatusVO : attributeValueStatusVOS) {
                if (attributeValueGroup.equals(attributeValueStatusVO.getValuePath())) {
                    ret = attributeValueStatusVO;
                    break;
                } else {
                    ret = getAttributeValueTreeNode( attributeValueGroup, attributeValueStatusVO.getChildren());
                }
            }
        }
        return ret;
    }

    /**
     * 填充属性值树结构
     * @param parent
     */
    public void fillAttributeValueTree(AttributeValueStatusVO parent,int hier,String[][] attributeValueArray)
    {
        if(parent.getChildren()==null)
        {
            parent.setChildren(new LinkedList<>());
        }
        for(int s=0;s<attributeValueArray[hier].length;s++)
        {
            AttributeValueStatusVO attributeValueStatusVO = new AttributeValueStatusVO();
            attributeValueStatusVO.setStatus(1);
            attributeValueStatusVO.setValue(attributeValueArray[hier][s]);
            attributeValueStatusVO.setValuePath(parent.getValuePath()+"_"+attributeValueStatusVO.getValue());
            if((hier+1)<attributeValueArray.length)
            {
                //填充这个属性值树结构
                fillAttributeValueTree(attributeValueStatusVO,hier+1,attributeValueArray);
            }
            attributeValueStatusVO.setParentValuePath(parent.getValuePath());
            parent.getChildren().add(attributeValueStatusVO);
        }
    }

    /**
     * 设置子节点禁用数量
     * @param current
     */
    public void setAttributeValueTreeDisabledCount(AttributeValueStatusVO current,List<AttributeValueStatusVO> children,List<AttributeValueStatusVO> attributeValueStatusVOS)
    {
        if(CollectionUtils.isNotEmpty(children))
        {
            for(AttributeValueStatusVO child:children) {
                setAttributeValueTreeDisabledCount(child,child.getChildren(),attributeValueStatusVOS);
            }
        }else{
            if(current.getStatus().intValue()==0)
            {
                if(current.getParentValuePath()!=null) {
                    AttributeValueStatusVO parent = getAttributeValueTreeNode(current.getParentValuePath(), attributeValueStatusVOS);
                    if(parent!=null) {
                        Integer disabledChildCount = parent.getDisabledChildCount();
                        disabledChildCount++;
                        parent.setDisabledChildCount(disabledChildCount);
                    }
                }
            }

        }
    }


    /**
     * 设置子节点禁用状态
     * @param current
     */
    public void setAttributeValueTreeDisabledStatus(AttributeValueStatusVO current)
    {
        if(CollectionUtils.isNotEmpty(current.getChildren()))
        {
            if(current.getDisabledChildCount().intValue()==current.getChildren().size())
            {
                current.setStatus(0);
            }
            for(AttributeValueStatusVO child:current.getChildren()) {
                setAttributeValueTreeDisabledStatus(child);
            }
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
