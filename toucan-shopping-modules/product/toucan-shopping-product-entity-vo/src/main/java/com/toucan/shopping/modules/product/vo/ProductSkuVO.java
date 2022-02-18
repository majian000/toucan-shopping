package com.toucan.shopping.modules.product.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.product.entity.ProductSku;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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
     * SKU商品主图
     */
    private MultipartFile mainPhotoFile;

    /**
     * 商品主图(HTTP访问)
     */
    private String httpMainPhoto;




}
