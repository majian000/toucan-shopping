package com.toucan.shopping.cloud.apps.web.controller.search;

import com.toucan.shopping.cloud.search.api.feign.service.FeignProductSearchService;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
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


    /**
     * 商品搜索 支持两种方式(GET、POST)
     * @param productSearchVO
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/search")
    public String search(ProductSearchVO productSearchVO, HttpServletRequest httpServletRequest){
        if(productSearchVO==null)
        {
            productSearchVO = new ProductSearchVO();
        }
        if(StringUtils.isEmpty(productSearchVO.getKeyword())||productSearchVO.getKeyword().length()>50)
        {
            productSearchVO.setKeyword("手机"); //默认关键字
        }
        try {
            productSearchVO.setSize(20);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), productSearchVO);
            ResultObjectVO resultObjectVO = feignProductSearchService.search(requestJsonVO);
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
            }
        }catch(Exception e)
        {
            logger.error(e.getMessage(),e);
        }
        return "search/product_list";
    }

}
