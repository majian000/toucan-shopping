package com.toucan.shopping.modules.product.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 发布商品VO
 */
@Data
public class ReleaseProductVO extends ShopProductVO {





    /**
     * 单价
     */
    private Double price;

    /**
     * 总数量
     */
    private Integer num;



    /**
     * 商品预览图
     */
    private MultipartFile[] previewPhotoFiles;

    /**
     * 品牌ID
     */
    private BrandVO brand;

    /**
     * SKU列表
     */
    private List<ProductSkuVO> productSkuVOList;



}
