package com.toucan.shopping.cloud.apps.h5.controller.product;

import com.toucan.shopping.cloud.search.api.ProductSearchServiceAPI;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * H5 商品搜索
 */
@RestController("h5ProductSearchController")
@RequestMapping("/h5/product")
public class ProductSearchController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private ProductSearchServiceAPI productSearchService;

    @Autowired
    private ImageUploadService imageUploadService;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResultObjectVO search(@RequestBody ProductSearchVO productSearchVO) {
        ResultObjectVO ret = new ResultObjectVO();
        try {
            if (productSearchVO == null) {
                productSearchVO = new ProductSearchVO();
            }
            if (productSearchVO.getSize() == null || productSearchVO.getSize() <= 0) {
                productSearchVO.setSize(20);
            }
            if (StringUtils.isEmpty(productSearchVO.getCid()) && StringUtils.isEmpty(productSearchVO.getKeyword())) {
                productSearchVO.setKeyword("手机"); // 默认关键字
            }

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), productSearchVO);
            ResultObjectVO resultObjectVO = productSearchService.search(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                PageInfo pageInfo = resultObjectVO.formatData(PageInfo.class);
                List<ProductSearchResultVO> productResult = pageInfo.formatDataList(ProductSearchResultVO.class);
                if (CollectionUtils.isNotEmpty(productResult)) {
                    for (ProductSearchResultVO r : productResult) {
                        r.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix() + r.getProductPreviewPath());
                    }
                }
                pageInfo.setList(productResult);
                ret.setData(pageInfo);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            ret.setCode(ResultObjectVO.FAILD);
            ret.setMsg("搜索失败,请稍后重试");
        }
        return ret;
    }
}
