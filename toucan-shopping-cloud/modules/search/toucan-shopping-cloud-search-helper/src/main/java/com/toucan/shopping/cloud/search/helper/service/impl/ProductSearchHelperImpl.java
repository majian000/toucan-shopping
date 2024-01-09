package com.toucan.shopping.cloud.search.helper.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyValueService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandService;
import com.toucan.shopping.cloud.search.helper.service.ProductSearchHelper;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.constant.ShopAttributeConstant;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.search.service.ProductSearchService;
import com.toucan.shopping.modules.search.vo.ProductSearchAttributeVO;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 商品搜索同步
 * @author
 */
@Service
public class ProductSearchHelperImpl implements ProductSearchHelper {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private ProductSearchService productSearchService;

    @Autowired
    private FeignCategoryService feignCategoryService;

    @Autowired
    private FeignBrandService feignBrandService;

    @Autowired
    private FeignAttributeKeyValueService feignAttributeKeyValueService;

    @Autowired
    private FeignShopCategoryService feignShopCategoryService;

    public void refresh(ProductSkuVO productSkuVO) throws Exception{
        if (productSkuVO.getDeleteStatus() != null
                && productSkuVO.getDeleteStatus().intValue() == 0
                && productSkuVO.getStatus() != null
                && productSkuVO.getStatus().intValue() == 1
                && productSkuVO.getStockNum().intValue() > 0) {
            List<ProductSearchResultVO> productSearchResultVOS = productSearchService.queryBySkuId(productSkuVO.getId());
            ProductSearchResultVO productSearchResultVO = new ProductSearchResultVO();
            productSearchResultVO.setId(productSkuVO.getId());
            productSearchResultVO.setSkuId(productSkuVO.getId());
            productSearchResultVO.setName(productSkuVO.getName());
            productSearchResultVO.setPrice(productSkuVO.getPrice());
            productSearchResultVO.setProductPreviewPath(productSkuVO.getProductPreviewPath());
            productSearchResultVO.setBrandId(productSkuVO.getBrandId());
            productSearchResultVO.setShopId(productSkuVO.getShopId());
            productSearchResultVO.setShopCategoryId(productSkuVO.getShopCategoryId());
            productSearchResultVO.setCategoryId(productSkuVO.getCategoryId());
            productSearchResultVO.setAttributeValueGroup(productSkuVO.getAttributeValueGroup());
            productSearchResultVO.setAttributes(new LinkedList<>());


            //设置搜索属性列表
            this.setSearchAttribute(productSearchResultVO,productSkuVO);

            //设置店铺搜索属性列表
            this.setSearchShopAttributeList(productSearchResultVO,productSkuVO);


            ProductSearchResultVO productSearchResultVOResult = null;
            if (!CollectionUtils.isEmpty(productSearchResultVOS)) {
                productSearchResultVOResult = productSearchResultVOS.get(0);
            }

            //查询品牌名称
            if (productSearchResultVOResult == null
                    || productSearchResultVOResult.getBrandId() == null
                    || productSearchResultVOResult.getBrandId().longValue() != productSearchResultVO.getBrandId().longValue()) {

                BrandVO queryBrand = new BrandVO();
                queryBrand.setId(productSkuVO.getBrandId());
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryBrand);
                ResultObjectVO resultObjectVO = feignBrandService.findById(requestJsonVO.sign(), requestJsonVO);
                if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                    List<BrandVO> brands = resultObjectVO.formatDataList(BrandVO.class);
                    if (CollectionUtils.isNotEmpty(brands)) {
                        BrandVO brandVO = brands.get(0);
                        productSearchResultVO.setBrandNameCN(brandVO.getChineseName());
                        productSearchResultVO.setBrandNameEN(brandVO.getEnglishName());
                        if (StringUtils.isNotEmpty(brandVO.getChineseName()) && StringUtils.isNotEmpty(brandVO.getEnglishName())) {
                            productSearchResultVO.setBrandName(brandVO.getChineseName() + "/" + brandVO.getEnglishName());
                        } else {
                            if (StringUtils.isNotEmpty(brandVO.getChineseName())) {
                                productSearchResultVO.setBrandName(brandVO.getChineseName());
                            }
                            if (StringUtils.isNotEmpty(brandVO.getEnglishName())) {
                                productSearchResultVO.setBrandName(brandVO.getEnglishName());
                            }
                        }
                    }
                }
            }else{
                productSearchResultVO.setBrandNameEN(productSearchResultVOResult.getBrandNameEN());
                productSearchResultVO.setBrandNameCN(productSearchResultVOResult.getBrandNameCN());
                productSearchResultVO.setBrandName(productSearchResultVOResult.getBrandName());
            }

            //查询分类名称
            if (productSearchResultVOResult == null
                    || productSearchResultVOResult.getCategoryId() == null
                    || productSearchResultVOResult.getCategoryId().longValue() != productSearchResultVO.getCategoryId().longValue()) {

                CategoryVO queryCategoryVO = new CategoryVO();
                queryCategoryVO.setId(productSkuVO.getCategoryId());
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryCategoryVO);
                ResultObjectVO resultObjectVO = feignCategoryService.queryById(requestJsonVO);
                if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                    CategoryVO categoryVO = resultObjectVO.formatData(CategoryVO.class);
                    //反转ID
                    Collections.reverse(categoryVO.getIdPath());
                    productSearchResultVO.setCategoryIds(new LinkedList<>());
                    for(Long categoryId:categoryVO.getIdPath())
                    {
                        productSearchResultVO.getCategoryIds().add(String.valueOf(categoryId));
                    }
                    productSearchResultVO.setCategoryName(categoryVO.getName());
                }
            }else{
                productSearchResultVO.setCategoryIds(productSearchResultVOResult.getCategoryIds());
                productSearchResultVO.setCategoryName(productSearchResultVOResult.getCategoryName());
            }

            this.setShopCategoryIdPath(productSearchResultVO);

            if (CollectionUtils.isEmpty(productSearchResultVOS)) {
                productSearchService.save(productSearchResultVO);
            } else {
                productSearchResultVO.setCreateDate(productSearchResultVOS.get(0).getCreateDate());
                productSearchService.update(productSearchResultVO);
            }
        }else if(productSkuVO.getDeleteStatus().intValue() == 1)
        {
            List<Long> deleteFaildList = new ArrayList<>();
            productSearchService.removeById(productSkuVO.getId(),deleteFaildList);
        }
    }


    /**
     * 设置搜索属性
     * @param productSearchResultVO
     */
    private void setSearchAttribute(ProductSearchResultVO productSearchResultVO, ProductSkuVO productSkuVO) throws NoSuchAlgorithmException {
        //设置属性列表
        productSkuVO.setAttributeMap(JSONObject.parseObject(productSkuVO.getAttributes(), HashMap.class));
        Set<String> keys = productSkuVO.getAttributeMap().keySet();
        List<String> queryAttributeKeys = new LinkedList<>();
        queryAttributeKeys.addAll(keys);
        List<String> queryAttributeValues = new LinkedList<>();
        for(String key:keys){
            ProductSearchAttributeVO productSearchAttributeVO = new ProductSearchAttributeVO();
            productSearchAttributeVO.setName(key);
            productSearchAttributeVO.setValue(productSkuVO.getAttributeMap().get(key));
            queryAttributeValues.add(productSkuVO.getAttributeMap().get(key));
            productSearchResultVO.getAttributes().add(productSearchAttributeVO);
        }

        AttributeKeyValueVO attributeKeyValueVO=new AttributeKeyValueVO();
        attributeKeyValueVO.setProductSpuId(productSkuVO.getProductId());
        attributeKeyValueVO.setCategoryId(productSkuVO.getCategoryId());
        attributeKeyValueVO.setAttributeKeyList(queryAttributeKeys);
        attributeKeyValueVO.setAttributeValueList(queryAttributeValues);
        ResultObjectVO resultObjectVO = feignAttributeKeyValueService.querySearchAttributeList(RequestJsonVOGenerator.generator(toucan.getAppCode(),attributeKeyValueVO));
        if(resultObjectVO.isSuccess())
        {
            List<AttributeKeyVO> attributeKeys = resultObjectVO.formatDataList(AttributeKeyVO.class);
            if(CollectionUtils.isNotEmpty(attributeKeys)){
                productSearchResultVO.setSearchAttributes(new LinkedList<>());
                for(AttributeKeyVO attributeKeyVO:attributeKeys)
                {
                    if(CollectionUtils.isNotEmpty(attributeKeyVO.getValues()))
                    {
                        for(AttributeValueVO attributeValueVO:attributeKeyVO.getValues()) {
                            ProductSearchAttributeVO productSearchAttributeVO = new ProductSearchAttributeVO();
                            productSearchAttributeVO.setNameId(attributeKeyVO.getId());
                            productSearchAttributeVO.setName(attributeKeyVO.getAttributeName());
                            productSearchAttributeVO.setValueId(attributeValueVO.getId());
                            productSearchAttributeVO.setValue(attributeValueVO.getAttributeValue());
                            productSearchResultVO.getSearchAttributes().add(productSearchAttributeVO);
                        }
                    }
                }
            }
        }
    }

    /**
     * 设置店铺搜索属性
     */
    private void setSearchShopAttributeList(ProductSearchResultVO productSearchResultVO, ProductSkuVO productSkuVO) throws InvocationTargetException, IllegalAccessException, NoSuchAlgorithmException {
        productSearchResultVO.setSearchShopAttributes(new LinkedList<>());
        //将搜索属性赋值到店铺搜索属性
        if(CollectionUtils.isNotEmpty(productSearchResultVO.getSearchAttributes())) {
            for(ProductSearchAttributeVO productSearchAttributeVO:productSearchResultVO.getSearchAttributes()) {
                ProductSearchAttributeVO productSearchShopAttributeVO = new ProductSearchAttributeVO();
                BeanUtils.copyProperties(productSearchShopAttributeVO,productSearchAttributeVO);
                productSearchResultVO.getSearchShopAttributes().add(productSearchShopAttributeVO);
            }
        }

        if(productSkuVO.getShopCategoryId()!=null) {
            ShopCategoryVO shopCategoryVO = new ShopCategoryVO();
            shopCategoryVO.setId(productSkuVO.getShopCategoryId());
            ResultObjectVO resultObjectVO = feignShopCategoryService.findById(RequestJsonVOGenerator.generator(toucan.getAppCode(), shopCategoryVO));
            if (resultObjectVO.isSuccess()) {
                List<ShopCategoryVO> shopCategoryVOS = resultObjectVO.formatDataList(ShopCategoryVO.class);
                if (CollectionUtils.isNotEmpty(shopCategoryVOS)) {
                    shopCategoryVO = shopCategoryVOS.get(0);
                    ProductSearchAttributeVO productSearchAttributeVO = new ProductSearchAttributeVO();
                    productSearchAttributeVO.setNameId(ShopAttributeConstant.SHOP_CATEGORY_ATTRIBUTE_KEY_ID);
                    productSearchAttributeVO.setName("商品分类");
                    productSearchAttributeVO.setValueId(shopCategoryVO.getId());
                    productSearchAttributeVO.setValue(shopCategoryVO.getName());
                    productSearchResultVO.getSearchShopAttributes().add(productSearchAttributeVO);
                }
            }
        }
    }

    /**
     * 设置店铺分类ID路径
     * @param productSearchResultVO
     * @throws Exception
     */
    private void setShopCategoryIdPath(ProductSearchResultVO productSearchResultVO) throws NoSuchAlgorithmException {
        ShopCategoryVO queryShopCateogry = new ShopCategoryVO();
        queryShopCateogry.setId(productSearchResultVO.getShopCategoryId());
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryShopCateogry);
        ResultObjectVO resultShopCategoryObjectVO = feignShopCategoryService.findIdPathById(requestJsonVO);
        if (resultShopCategoryObjectVO.isSuccess() && resultShopCategoryObjectVO.getData() != null) {
            ShopCategoryVO shopCategoryVO = resultShopCategoryObjectVO.formatData(ShopCategoryVO.class);
            List<String> shopCategoryIdPath = new LinkedList<>();
            if (CollectionUtils.isNotEmpty(shopCategoryVO.getIdPath())) {
                for (Long shopCategoryId : shopCategoryVO.getIdPath()) {
                    shopCategoryIdPath.add(String.valueOf(shopCategoryId));
                }
                productSearchResultVO.setShopCategoryIds(shopCategoryIdPath);
            }
        }
    }

}
