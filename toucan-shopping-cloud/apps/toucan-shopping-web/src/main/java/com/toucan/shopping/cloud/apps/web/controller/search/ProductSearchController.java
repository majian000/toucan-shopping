package com.toucan.shopping.cloud.apps.web.controller.search;

import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandService;
import com.toucan.shopping.cloud.search.api.feign.service.FeignProductSearchService;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;
import com.toucan.shopping.modules.product.vo.BrandVO;
import com.toucan.shopping.modules.search.vo.ProductSearchAttributeVO;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import com.toucan.shopping.modules.search.vo.ProductSearchVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品搜索
 */
@Controller("productSearchController")
@RequestMapping("/api/product")
public class ProductSearchController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignProductSearchService feignProductSearchService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignAttributeKeyService feignAttributeKeyService;

    @Autowired
    private FeignBrandService feignBrandService;

    private String doSearch(ProductSearchVO productSearchVO, HttpServletRequest httpServletRequest)
    {
        try {
            RequestJsonVO requestJsonVO =null;
            ResultObjectVO resultObjectVO = null;
            /**
             * 优先级先查询品牌,如果关键字能匹配上品牌就查询出该品牌下所有商品
             * 如果不能匹配上匹配就关键字全量搜索
             */
            List<BrandVO> hitBrands = new ArrayList<>();
            if("t".equals(productSearchVO.getQbs())) {
                BrandVO queryBrandVO = new BrandVO();
                queryBrandVO.setCategoryId(productSearchVO.getCid());
                queryBrandVO.setName(productSearchVO.getKeyword());
                queryBrandVO.setEnabledStatus(1);
                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryBrandVO);
                resultObjectVO = feignBrandService.findListByNameAndCategoryIdAndEnabled(requestJsonVO);
                if (resultObjectVO.isSuccess()) {
                    List<BrandVO> brands = resultObjectVO.formatDataList(BrandVO.class);
                    String[] ebids = null; //不查询这个品牌的商品
                    if (StringUtils.isNotEmpty(productSearchVO.getEbids())) {
                        ebids = productSearchVO.getEbids().split(",");
                    }

                    if (CollectionUtils.isNotEmpty(brands)) {
                        for (BrandVO brandVO : brands) {
                            if (StringUtils.isNotEmpty(brandVO.getChineseName()) && StringUtils.isNotEmpty(brandVO.getEnglishName())) {
                                brandVO.setName(brandVO.getChineseName() + "/" + brandVO.getEnglishName());
                            } else if (StringUtils.isNotEmpty(brandVO.getChineseName())) {
                                brandVO.setName(brandVO.getChineseName());
                            } else if (StringUtils.isNotEmpty(brandVO.getEnglishName())) {
                                brandVO.setName(brandVO.getEnglishName());
                            }
                            if (ebids != null) {
                                for (String ebid : ebids) {
                                    if (!ebid.equals(String.valueOf(brandVO.getId()))) {
                                        hitBrands.add(brandVO);
                                    }
                                }
                            } else {
                                hitBrands.add(brandVO);
                            }
                        }
                    }
                    if (CollectionUtils.isNotEmpty(hitBrands)) {
                        productSearchVO.setBrandIds(hitBrands.stream().map(BrandVO::getId).distinct().collect(Collectors.toList()));
                    }
                }
            }

            if(StringUtils.isNotEmpty(productSearchVO.getBid())&&StringUtils.isNotEmpty(productSearchVO.getBn()))
            {
                BrandVO brandVO = new BrandVO();
                brandVO.setId(Long.parseLong(productSearchVO.getBid()));
                brandVO.setName(productSearchVO.getBn());
                hitBrands.add(brandVO);
            }

            httpServletRequest.setAttribute("hitBrands", hitBrands);

            //选择的查询属性
            if(StringUtils.isNotEmpty(productSearchVO.getAb())&&StringUtils.isNotEmpty(productSearchVO.getAbids()))
            {
                productSearchVO.setSearchAttributes(new LinkedList<>());
                String[] abArray = productSearchVO.getAb().split(",");
                String[] abidsArray = productSearchVO.getAbids().split(",");
                if(abArray!=null&&abidsArray!=null&&abidsArray.length==abidsArray.length)
                {
                    for(int i=0;i<abArray.length;i++) {
                        String[] akv = abArray[i].split(":");
                        ProductSearchAttributeVO productSearchAttributeVO = new ProductSearchAttributeVO();
                        productSearchAttributeVO.setName(akv[0]);
                        productSearchAttributeVO.setValue(akv[1]);
                        productSearchAttributeVO.setNameId(Long.parseLong(abidsArray[i]));
                        productSearchVO.getSearchAttributes().add(productSearchAttributeVO);
                    }
                }
            }

            httpServletRequest.setAttribute("keyword",productSearchVO.getKeyword());
            //分类ID
            httpServletRequest.setAttribute("cid",productSearchVO.getCid());
            //将这些品牌ID排除出查询条件
            httpServletRequest.setAttribute("ebids",productSearchVO.getEbids());
            //是否先查询品牌
            httpServletRequest.setAttribute("qbs",productSearchVO.getQbs());
            //属性ID列表
            httpServletRequest.setAttribute("abids",productSearchVO.getAbids());
            //属性集合字符串
            httpServletRequest.setAttribute("ab",productSearchVO.getAb());
            //属性集合
            httpServletRequest.setAttribute("selectSearchAttributes",productSearchVO.getSearchAttributes());
            //品牌ID
            httpServletRequest.setAttribute("bid",productSearchVO.getBid());
            //品牌名称
            httpServletRequest.setAttribute("bn",productSearchVO.getBn());
            //价格区间
            httpServletRequest.setAttribute("ps",productSearchVO.getPs());
            httpServletRequest.setAttribute("pe",productSearchVO.getPe());

            if(StringUtils.isNotEmpty(productSearchVO.getPs()))
            {
                try{
                    productSearchVO.setPsd(Double.parseDouble(productSearchVO.getPs()));
                }catch(Exception e)
                {
                    logger.error(e.getMessage(),e);
                }
            }

            if(StringUtils.isNotEmpty(productSearchVO.getPe()))
            {
                try{
                    productSearchVO.setPed(Double.parseDouble(productSearchVO.getPe()));
                }catch(Exception e)
                {
                    logger.error(e.getMessage(),e);
                }
            }

            productSearchVO.setSize(20);
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), productSearchVO);
            resultObjectVO = feignProductSearchService.search(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                PageInfo pageInfo = resultObjectVO.formatData(PageInfo.class);
                List<ProductSearchResultVO> productResult = pageInfo.formatDataList(ProductSearchResultVO.class);
                if(CollectionUtils.isNotEmpty(productResult))
                {
                    for(ProductSearchResultVO productSearchResultVO:productResult)
                    {
                        productSearchResultVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix()+productSearchResultVO.getProductPreviewPath());
                    }
                }
                httpServletRequest.setAttribute("productResult",productResult);
                httpServletRequest.setAttribute("page",pageInfo.getPage());
                httpServletRequest.setAttribute("total",pageInfo.getTotal());
                httpServletRequest.setAttribute("pageTotal",pageInfo.getPageTotal());

                //查询搜索属性列表
                if(CollectionUtils.isNotEmpty(productResult)) {
                    AttributeKeyVO attributeKeyVO = new AttributeKeyVO();
                    if(StringUtils.isNotEmpty(productSearchVO.getCid()))
                    {
                        attributeKeyVO.setCategoryId(Long.parseLong(productSearchVO.getCid()));
                    }else {
                        attributeKeyVO.setCategoryId(productResult.get(0).getCategoryId());
                    }
                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),attributeKeyVO);
                    resultObjectVO = feignAttributeKeyService.querySearchList(requestJsonVO);
                    if(resultObjectVO.isSuccess())
                    {
                        List<AttributeKeyVO> attributes= resultObjectVO .formatDataList(AttributeKeyVO.class);
                        if(CollectionUtils.isEmpty(productSearchVO.getSearchAttributes()))
                        {
                            httpServletRequest.setAttribute("searchAttributes",attributes);
                        }else {
                            List<AttributeKeyVO> releaseAttributes = new LinkedList<>();
                            boolean isSelectAttribute=false;
                            if (CollectionUtils.isNotEmpty(attributes)) {
                                for (AttributeKeyVO akv : attributes) {
                                    isSelectAttribute=false;
                                    for(ProductSearchAttributeVO selectSearchAttribute:productSearchVO.getSearchAttributes())
                                    {
                                        if(selectSearchAttribute.getNameId().equals(akv.getId()))
                                        {
                                            isSelectAttribute=true;
                                            break;
                                        }
                                    }
                                    if(!isSelectAttribute)
                                    {
                                        releaseAttributes.add(akv);
                                    }
                                }
                            }
                            httpServletRequest.setAttribute("searchAttributes",releaseAttributes);
                        }
                    }
                }


                //查询品牌列表(没有查询到指定品牌),就查询出所有可查询的品牌
                if(CollectionUtils.isEmpty(hitBrands))
                {
                    BrandVO queryBrandVO = new BrandVO();
                    if(StringUtils.isNotEmpty(productSearchVO.getCid()))
                    {
                        queryBrandVO.setCategoryId(productSearchVO.getCid());
                    }else {
                        queryBrandVO.setCategoryId(String.valueOf(productResult.get(0).getCategoryId()));
                    }
                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryBrandVO);
                    resultObjectVO = feignBrandService.queryListByCategoryId(requestJsonVO);
                    if(resultObjectVO.isSuccess())
                    {
                        List<BrandVO> brandVOS= resultObjectVO .formatDataList(BrandVO.class);
                        if(!CollectionUtils.isEmpty(brandVOS)){
                            for(BrandVO brandVO:brandVOS) {
                                if (StringUtils.isNotEmpty(brandVO.getChineseName()) && StringUtils.isNotEmpty(brandVO.getEnglishName())) {
                                    brandVO.setName(brandVO.getChineseName() + "/" + brandVO.getEnglishName());
                                } else {
                                    if (StringUtils.isNotEmpty(brandVO.getChineseName())) {
                                        brandVO.setName(brandVO.getChineseName());
                                    } else {
                                        brandVO.setName(brandVO.getEnglishName());
                                    }
                                }
                            }
                            httpServletRequest.setAttribute("searchBrands",brandVOS);
                        }
                    }
                }
            }



        }catch(Exception e)
        {
            logger.error(e.getMessage(),e);
        }
        return "search/product_list";
    }

    /**
     * 商品搜索 支持两种方式
     *  /p/search POST
     *  /g/search GET
     * @param productSearchVO
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/p/search",method = RequestMethod.POST)
    public String searchByPost(ProductSearchVO productSearchVO, HttpServletRequest httpServletRequest){
        if(productSearchVO==null)
        {
            productSearchVO = new ProductSearchVO();
        }
        if(StringUtils.isEmpty(productSearchVO.getCid())) {
            if (StringUtils.isEmpty(productSearchVO.getKeyword()) || productSearchVO.getKeyword().length() > 50) {
                productSearchVO.setKeyword("手机"); //默认关键字
            }
        }
        return this.doSearch(productSearchVO,httpServletRequest);
    }

    /**
     * 商品搜索 支持两种方式
     *  /p/search POST
     *  /g/search GET
     * @param productSearchVO
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/g/search",method = RequestMethod.GET)
    public String searchByGet(ProductSearchVO productSearchVO, HttpServletRequest httpServletRequest){
        if(productSearchVO==null)
        {
            productSearchVO = new ProductSearchVO();
        }
        if(StringUtils.isEmpty(productSearchVO.getCid())) {
            if (StringUtils.isEmpty(productSearchVO.getKeyword()) || productSearchVO.getKeyword().length() > 50) {
                productSearchVO.setKeyword("手机"); //默认关键字
            }
        }
        return this.doSearch(productSearchVO,httpServletRequest);
    }



}
