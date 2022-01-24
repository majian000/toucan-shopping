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
     * 商品名称
     */
    private String productName;

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
     * 商品主图
     */
    private MultipartFile mainPhotoFile;

    /**
     * 商品主图(HTTP访问)
     */
    private String httpMainPhoto;


    /**
     * 品牌ID
     */
    private BrandVO brand;

    /**
     * 货号
     */
    private String articleNumber;


}
