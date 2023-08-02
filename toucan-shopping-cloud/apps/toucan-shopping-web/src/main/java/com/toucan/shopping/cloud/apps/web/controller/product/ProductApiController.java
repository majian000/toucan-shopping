package com.toucan.shopping.cloud.apps.web.controller.product;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ql.util.express.DefaultContext;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSpuService;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.qlexpress.service.QLExpressService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @Autowired
    private FeignCategoryService feignCategoryService;

    @Autowired
    private FeignBrandService feignBrandService;

    @Autowired
    private QLExpressService qlExpressService;


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

                    computeProductPrice(productSkuVO);

                    setAttributeDisabled(productSkuVO);

                    this.setCategoryBrands(productSkuVO);

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
     * 通过规则引擎计算商品金额
     * @param productSkuVO
     * @throws Exception
     */
    void computeProductPrice(ProductSkuVO productSkuVO) throws Exception
    {
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        context.put("price", productSkuVO.getPrice());
        String price = String.valueOf(qlExpressService.execute("price",context));
        productSkuVO.setPrice(new BigDecimal(price));
    }



    /**
     * 设置分类品牌路径
     * @param productSkuVO
     * @throws Exception
     */
    private void setCategoryBrands(ProductSkuVO productSkuVO) throws Exception
    {

        //查询分类与品牌路径
        CategoryVO queryCateogry = new CategoryVO();
        queryCateogry.setId(productSkuVO.getCategoryId());
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryCateogry);
        //查询分类
        ResultObjectVO resultCategoryObjectVO = feignCategoryService.findIdPathById(requestJsonVO);
        List<ProductSkuCategoryBrandVO> productSkuCategoryBrands= new LinkedList<>();
        if(resultCategoryObjectVO.isSuccess())
        {
            CategoryVO categoryVO = resultCategoryObjectVO.formatData(CategoryVO.class);
            if (CollectionUtils.isNotEmpty(categoryVO.getIdPath())&&CollectionUtils.isNotEmpty(categoryVO.getNamePaths())) {
                for(int i=categoryVO.getIdPath().size()-1;i>=0;i--)
                {
                    ProductSkuCategoryBrandVO productSkuCategoryBrandVO=new ProductSkuCategoryBrandVO();
                    productSkuCategoryBrandVO.setId(categoryVO.getIdPath().get(i));
                    productSkuCategoryBrandVO.setName(categoryVO.getNamePaths().get(i));
                    productSkuCategoryBrandVO.setType(1);
                    productSkuCategoryBrands.add(productSkuCategoryBrandVO);
                }
            }
        }

        BrandVO brandVO = new BrandVO();
        brandVO.setId(productSkuVO.getBrandId());
        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), brandVO);
        ResultObjectVO brandResultVO = feignBrandService.findById(requestJsonVO.sign(), requestJsonVO);
        if (brandResultVO.isSuccess()) {
            List<BrandVO> brandVOS = brandResultVO.formatDataList(BrandVO.class);
            if(CollectionUtils.isNotEmpty(brandVOS)) {
                brandVO = brandVOS.get(0);
                ProductSkuCategoryBrandVO productSkuCategoryBrandVO = new ProductSkuCategoryBrandVO();
                if (StringUtils.isNotEmpty(brandVO.getChineseName()) && StringUtils.isNotEmpty(brandVO.getEnglishName())) {
                    productSkuCategoryBrandVO.setName(brandVO.getChineseName() + "/" + brandVO.getEnglishName());
                } else {
                    if (StringUtils.isNotEmpty(brandVO.getChineseName())) {
                        productSkuCategoryBrandVO.setName(brandVO.getChineseName());
                    }
                    if (StringUtils.isNotEmpty(brandVO.getEnglishName())) {
                        productSkuCategoryBrandVO.setName(brandVO.getEnglishName());
                    }
                }
                productSkuCategoryBrandVO.setId(brandVO.getId());
                productSkuCategoryBrandVO.setType(2);
                productSkuCategoryBrands.add(productSkuCategoryBrandVO);
            }
        }

        productSkuVO.setCategoryBrands(productSkuCategoryBrands);
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
            List<ProductSkuStatusVO> skuBuyStatusVOS = productSkuVO.getSkuStatusList();
            for (int i = 0; i < skuBuyStatusVOS.size(); i++) {
                ProductSkuStatusVO productSkuStatusVO = skuBuyStatusVOS.get(i);
                isDisabled = false;

                if (productSkuStatusVO.getStatus() != null && productSkuStatusVO.getStatus().intValue() == 0) {
                    isDisabled = true;
                }
                if (productSkuStatusVO.getStockNum() != null && productSkuStatusVO.getStockNum().intValue() <= 0) {
                    isDisabled = true;
                }
                if(isDisabled) {
                    AttributeValueStatusVO attributeValueStatusVO = getAttributeValueTreeNode(productSkuStatusVO.getAttributeValueGroup(), attributeValueStatusVOS);
                    if (attributeValueStatusVO != null) {
                        //设置禁用
                        if (productSkuStatusVO.getStatus() != null && productSkuStatusVO.getStatus().intValue() == 0) {
                            attributeValueStatusVO.setStatus(0);
                            attributeValueStatusVO.setStatusCode(2);
                        }
                        if (productSkuStatusVO.getStockNum() != null && productSkuStatusVO.getStockNum().intValue() <= 0) {
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
            productSkuVO.setSkuStatusList(null);
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
                    if(ret!=null)
                    {
                        return ret;
                    }
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
                if(child.getParentValuePath()!=null&&child.getDisabledChildCount().intValue()>0
                        &&child.getDisabledChildCount().intValue()==child.getChildren().size()) {
                    AttributeValueStatusVO parent = getAttributeValueTreeNode(child.getParentValuePath(), attributeValueStatusVOS);
                    if(parent!=null) {
                        Integer disabledChildCount = parent.getDisabledChildCount();
                        disabledChildCount++;
                        parent.setDisabledChildCount(disabledChildCount);
                    }
                }
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
            if(StringUtils.isNotEmpty(shopProductVO.getAttrPath()))
            {
                queryShopProduct.setAttrPath(shopProductVO.getAttrPath());
            }
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


                    computeProductPrice(productSkuVO);

                    setAttributeDisabled(productSkuVO);

                    this.setCategoryBrands(productSkuVO);

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
