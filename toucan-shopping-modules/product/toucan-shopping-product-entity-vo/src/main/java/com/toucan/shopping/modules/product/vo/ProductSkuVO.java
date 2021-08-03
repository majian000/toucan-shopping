package com.toucan.shopping.modules.product.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.product.entity.ProductSku;
import lombok.Data;

import java.util.Date;

/**
 * 商品SKU VO
 *
 * @author majian
 * @date 2021-8-3 09:23:31
 */
@Data
public class ProductSkuVO  extends ProductSku {

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 封面预览图
     */
    private String previewImg;


    /**
     * HTTP访问封面预览图
     */
    private String httpPreviewImg;

}
