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
            BrandVO queryBrandVO = new BrandVO();
            queryBrandVO.setCategoryId(productSearchVO.getCid());
            queryBrandVO.setName(productSearchVO.getKeyword());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryBrandVO);
            ResultObjectVO resultObjectVO = feignBrandService.findListByNameAndCategoryId(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<BrandVO> brands = resultObjectVO.formatDataList(BrandVO.class);
                if(CollectionUtils.isNotEmpty(brands))
                {
                    for(BrandVO brandVO:brands)
                    {
                        if(StringUtils.isNotEmpty(brandVO.getChineseName())&&StringUtils.isNotEmpty(brandVO.getEnglishName()))
                        {
                            brandVO.setName(brandVO.getChineseName() + "/"+brandVO.getEnglishName());
                        }else if(StringUtils.isNotEmpty(brandVO.getChineseName())){
                            brandVO.setName(brandVO.getChineseName());
                        }else if(StringUtils.isNotEmpty(brandVO.getEnglishName())){
                            brandVO.setName(brandVO.getEnglishName());
                        }
                    }
                    httpServletRequest.setAttribute("brands",brands);
                    productSearchVO.setBrandIds(brands.stream().map(BrandVO::getId).distinct().collect(Collectors.toList()));
                }
            }

            httpServletRequest.setAttribute("keyword",productSearchVO.getKeyword());
            httpServletRequest.setAttribute("cid",productSearchVO.getCid());
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
                        httpServletRequest.setAttribute("attributes",resultObjectVO .formatDataList(AttributeKeyVO.class));
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
