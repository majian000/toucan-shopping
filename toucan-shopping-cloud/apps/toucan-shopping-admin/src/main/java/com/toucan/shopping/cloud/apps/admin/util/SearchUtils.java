package com.toucan.shopping.cloud.apps.admin.util;

import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandService;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.vo.BrandVO;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.search.service.ProductSearchService;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SearchUtils {


    public static ProductSearchService productSearchService;

    public static Toucan toucan;

    public static FeignCategoryService feignCategoryService;

    public static FeignBrandService feignBrandService;

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
            List<ProductSearchResultVO> productSearchResultVOS = productSearchService.queryBySkuId(productSkuVO.getId());
            ProductSearchResultVO productSearchResultVO = new ProductSearchResultVO();
            productSearchResultVO.setId(productSkuVO.getId());
            productSearchResultVO.setSkuId(productSkuVO.getId());
            productSearchResultVO.setName(productSkuVO.getName());
            productSearchResultVO.setPrice(productSkuVO.getPrice());
            productSearchResultVO.setProductPreviewPath(productSkuVO.getProductPreviewPath());
            productSearchResultVO.setBrandId(productSkuVO.getBrandId());
            productSearchResultVO.setCategoryId(productSkuVO.getCategoryId());
            productSearchResultVO.setAttributeValueGroup(productSkuVO.getAttributeValueGroup());

            ProductSearchResultVO productSearchResultVOResult = null;
            if (!CollectionUtils.isEmpty(productSearchResultVOS)) {
                productSearchResultVOResult = productSearchResultVOS.get(0);
            }

            //查询品牌信息
            BrandVO queryBrand = new BrandVO();
            queryBrand.setId(productSkuVO.getBrandId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryBrand);
            ResultObjectVO resultObjectVO = feignBrandService.findById(requestJsonVO.sign(), requestJsonVO);
            if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                List<BrandVO> brands = resultObjectVO.formatDataList(BrandVO.class);
                if (CollectionUtils.isNotEmpty(brands)) {
                    BrandVO brandVO = brands.get(0);
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
                productSearchService.save(productSearchResultVO);
            } else {
                productSearchService.update(productSearchResultVO);
            }
        }else if(productSkuVO.getDeleteStatus().intValue() == 1)
        {
            List<Long> deleteFaildList = new ArrayList<>();
            productSearchService.removeById(productSkuVO.getId(),deleteFaildList);
        }

    }
}