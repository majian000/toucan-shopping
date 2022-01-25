package com.toucan.shopping.modules.product.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 发布商品VO
 */
@Data
public class ReleaseProductVO {


    /**
     * 商品所属分类
     */
    private Long categoryId;

    /**
     * 店铺内的所属分类
     */
    private Long shopCategoryId;


    /**
     * 单价
     */
    private Double price;

    /**
     * 总数量
     */
    private Integer num;

    /**
     * 商家编码
     */
    private String sellerNo;

    /**
     * 付款方式(1:一口价(普通交易模式))
     */
    private Short payMethod;

    /**
     * 库存计数 (1:买家拍下减库存 2:买家付款减库存)
     */
    private Short buckleInventoryMethod;

    /**
     * 售后服务1:提供发票
     */
    private Short provideInvoice;

    /**
     * 售后服务1:退换货承诺
     */
    private Short changingOrRefunding;

    /**
     * 商品预览图
     */
    private MultipartFile[] previewPhotoFiles;

    /**
     * 品牌ID
     */
    private BrandVO brand;

    /**
     * 货号
     */
    private String articleNumber;


}
