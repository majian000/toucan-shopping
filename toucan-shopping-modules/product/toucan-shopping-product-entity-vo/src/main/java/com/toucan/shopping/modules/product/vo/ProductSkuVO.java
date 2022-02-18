package com.toucan.shopping.modules.product.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.product.entity.ProductSku;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Map;

/**
 * 商品SKU VO
 *
 * @author majian
 * @date 2021-8-3 09:23:31
 */
@Data
public class ProductSkuVO  extends ProductSku {

    /**
     * 属性映射
     */
    private Map<String,String> attributeMap;


    /**
     * SKU商品主图
     */
    @JSONField(serialize = false)
    private MultipartFile mainPhotoFile;

    /**
     * SKU商品主图路径
     */
    private String mainPhotoFilePath;

    /**
     * 商品主图(HTTP访问)
     */
    private String httpMainPhoto;




}
