package com.toucan.shopping.modules.product.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 发布商品VO
 */
@Data
public class PublishProductApproveVO extends ShopProductApproveVO {


    /**
     * 商品主图
     */
    @JSONField(serialize = false)
    private MultipartFile mainPhotoFile;


    /**
     * 单价
     */
    private Double price;

    /**
     * 总数量
     */
    private Integer num;



    /**
     * 品牌ID
     */
    private BrandVO brand;




}
