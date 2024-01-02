package com.toucan.shopping.cloud.apps.admin.util;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyValueService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandService;
import com.toucan.shopping.cloud.search.api.feign.service.FeignProductSearchService;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.search.vo.ProductSearchAttributeVO;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class SearchUtils {


    public static FeignProductSearchService feignProductSearchService;

    public static Toucan toucan;

    public static FeignCategoryService feignCategoryService;

    public static FeignBrandService feignBrandService;

    public static FeignAttributeKeyValueService feignAttributeKeyValueService;

    /**
     * 刷新到搜索
     * @param productSkuVO
     */
    public static void flushToSearch(ProductSkuVO productSkuVO) throws Exception
    {

        if (productSkuVO.getDeleteStatus() != null
                && productSkuVO.getDeleteStatus().intValue() == 0
                && productSkuVO.getStatus() != null
                && productSkuVO.getStatus().intValue() == 1
                && productSkuVO.getStockNum().intValue() > 0) {

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), productSkuVO.getId());
            ResultObjectVO resultObjectVO  = feignProductSearchService.queryBySkuId(requestJsonVO);
            if(!resultObjectVO.isSuccess())
            {
                throw new IllegalArgumentException(resultObjectVO.getMsg());
            }
            List<ProductSearchResultVO> productSearchResultVOS = resultObjectVO.formatDataList(ProductSearchResultVO.class);
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

            //设置搜索属性列表
            AttributeKeyValueVO attributeKeyValueVO=new AttributeKeyValueVO();
            attributeKeyValueVO.setProductSpuId(productSkuVO.getProductId());
            attributeKeyValueVO.setCategoryId(productSkuVO.getCategoryId());
            attributeKeyValueVO.setAttributeKeyList(queryAttributeKeys);
            attributeKeyValueVO.setAttributeValueList(queryAttributeValues);
            resultObjectVO = feignAttributeKeyValueService.querySearchAttributeList(RequestJsonVOGenerator.generator(toucan.getAppCode(),attributeKeyValueVO));
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

            //查询品牌信息
            BrandVO queryBrand = new BrandVO();
            queryBrand.setId(productSkuVO.getBrandId());
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryBrand);
            resultObjectVO = feignBrandService.findById(requestJsonVO.sign(), requestJsonVO);
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

            //查询分类
            CategoryVO queryCategoryVO = new CategoryVO();
            queryCategoryVO.setId(productSkuVO.getCategoryId());
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryCategoryVO);
            resultObjectVO = feignCategoryService.queryById(requestJsonVO);
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

            if (CollectionUtils.isEmpty(productSearchResultVOS)) {
                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), productSearchResultVO);
                feignProductSearchService.save(requestJsonVO);
            } else {
                productSearchResultVO.setCreateDate(productSearchResultVOS.get(0).getCreateDate());
                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), productSearchResultVO);
                feignProductSearchService.update(requestJsonVO);
            }
        }else if(productSkuVO.getDeleteStatus().intValue() == 1)
        {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), productSkuVO.getId());
            feignProductSearchService.removeById(requestJsonVO);
        }

    }
}
