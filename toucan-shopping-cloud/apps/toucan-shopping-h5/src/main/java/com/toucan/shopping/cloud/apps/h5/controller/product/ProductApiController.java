package com.toucan.shopping.cloud.apps.h5.controller.product;

import com.ql.util.express.DefaultContext;
import com.toucan.shopping.cloud.common.data.api.CategoryServiceAPI;
import com.toucan.shopping.cloud.product.api.BrandServiceAPI;
import com.toucan.shopping.cloud.product.api.ProductSkuServiceAPI;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.product.vo.BrandVO;
import com.toucan.shopping.modules.product.vo.ProductSkuCategoryBrandVO;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.product.vo.ShopProductDescriptionImgVO;
import com.toucan.shopping.modules.qlexpress.service.QLExpressService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * H5 商品详情
 */
@RestController("h5ProductController")
@RequestMapping("/h5/product")
public class ProductApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private ProductSkuServiceAPI productSkuService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private CategoryServiceAPI categoryService;

    @Autowired
    private BrandServiceAPI brandService;

    @Autowired
    private QLExpressService qlExpressService;

    /**
     * 商品详情（按 SKU id）
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultObjectVO detail(@RequestBody ProductSkuVO productSkuVO) {
        ResultObjectVO retObject = new ResultObjectVO();
        try {
            ProductSkuVO query = new ProductSkuVO();
            query.setId(productSkuVO.getId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), query);
            ResultObjectVO resultObjectVO = productSkuService.queryByIdForFront(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    productSkuVO = resultObjectVO.formatData(ProductSkuVO.class);

                    // 主图
                    if (productSkuVO.getMainPhotoFilePath() != null) {
                        productSkuVO.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix() + productSkuVO.getMainPhotoFilePath());
                    }
                    // 预览图
                    if (CollectionUtils.isNotEmpty(productSkuVO.getPreviewPhotoPaths())) {
                        productSkuVO.setHttpPreviewPhotoPaths(new LinkedList<>());
                        for (String path : productSkuVO.getPreviewPhotoPaths()) {
                            productSkuVO.getHttpPreviewPhotoPaths().add(imageUploadService.getImageHttpPrefix() + path);
                        }
                    }
                    // 商品介绍图
                    if (productSkuVO.getShopProductDescriptionVO() != null
                            && CollectionUtils.isNotEmpty(productSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs())) {
                        for (ShopProductDescriptionImgVO img : productSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs()) {
                            img.setHttpFilePath(imageUploadService.getImageHttpPrefix() + img.getFilePath());
                        }
                    }
                    // 商品主图路径（SKU 预览）
                    if (productSkuVO.getProductPreviewPath() != null) {
                        productSkuVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix() + productSkuVO.getProductPreviewPath());
                    }

                    computeProductPrice(productSkuVO);
                    setCategoryBrands(productSkuVO);

                    retObject.setData(productSkuVO);
                }
            }
        } catch (Exception e) {
            retObject.setMsg("查询失败,请稍后重试");
            retObject.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return retObject;
    }

    /** 通过规则引擎计算商品金额 */
    private void computeProductPrice(ProductSkuVO productSkuVO) throws Exception {
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("price", productSkuVO.getPrice());
        String price = String.valueOf(qlExpressService.execute("price", context));
        productSkuVO.setPrice(new BigDecimal(price));
    }

    /** 设置分类品牌路径 */
    private void setCategoryBrands(ProductSkuVO productSkuVO) throws Exception {
        List<ProductSkuCategoryBrandVO> categoryBrands = new LinkedList<>();

        CategoryVO queryCategory = new CategoryVO();
        queryCategory.setId(productSkuVO.getCategoryId());
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryCategory);
        ResultObjectVO categoryResult = categoryService.findIdPathById(requestJsonVO);
        if (categoryResult.isSuccess()) {
            CategoryVO categoryVO = categoryResult.formatData(CategoryVO.class);
            if (CollectionUtils.isNotEmpty(categoryVO.getIdPath()) && CollectionUtils.isNotEmpty(categoryVO.getNamePaths())) {
                for (int i = categoryVO.getIdPath().size() - 1; i >= 0; i--) {
                    ProductSkuCategoryBrandVO item = new ProductSkuCategoryBrandVO();
                    item.setId(categoryVO.getIdPath().get(i));
                    item.setName(categoryVO.getNamePaths().get(i));
                    item.setType(1);
                    categoryBrands.add(item);
                }
            }
        }

        BrandVO brandVO = new BrandVO();
        brandVO.setId(productSkuVO.getBrandId());
        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), brandVO);
        ResultObjectVO brandResult = brandService.findById(requestJsonVO);
        if (brandResult.isSuccess()) {
            List<BrandVO> brandVOS = brandResult.formatDataList(BrandVO.class);
            if (CollectionUtils.isNotEmpty(brandVOS)) {
                brandVO = brandVOS.get(0);
                ProductSkuCategoryBrandVO item = new ProductSkuCategoryBrandVO();
                if (StringUtils.isNotEmpty(brandVO.getChineseName()) && StringUtils.isNotEmpty(brandVO.getEnglishName())) {
                    item.setName(brandVO.getChineseName() + "/" + brandVO.getEnglishName());
                } else if (StringUtils.isNotEmpty(brandVO.getChineseName())) {
                    item.setName(brandVO.getChineseName());
                } else if (StringUtils.isNotEmpty(brandVO.getEnglishName())) {
                    item.setName(brandVO.getEnglishName());
                }
                item.setId(brandVO.getId());
                item.setType(2);
                categoryBrands.add(item);
            }
        }

        productSkuVO.setCategoryBrands(categoryBrands);
    }
}
