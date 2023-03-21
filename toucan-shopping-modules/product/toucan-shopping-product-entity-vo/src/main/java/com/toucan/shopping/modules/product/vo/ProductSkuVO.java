package com.toucan.shopping.modules.product.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.product.entity.ProductSku;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
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
     * 商品主图(HTTP访问)
     */
    private String httpMainPhoto;


    private String categoryName; //类别名称
    private String categoryPath; //类别路径

    private String shopCategoryName; //类别名称
    private String shopCategoryPath; //类别路径

    private String brandChineseName; //品牌中文名称
    private String brandEnglishName; //品牌英文名称
    private String brandLogo; //品牌logo
    private String brandHttpLogo; //品牌LOGO地址

    private String shopName; //店铺名称
    private Long brandId; //品牌ID




    private String productAttributes; //这个商品所有属性



    private String httpProductPreviewPath; //商品主图HTTP路径

    /**
     * 商品预览图路径
     */
    private List<String> previewPhotoPaths;


    /**
     * 商品预览图路径
     */
    private List<String> httpPreviewPhotoPaths;


    /**
     * SKU预览图路径
     */
    private List<String> httpSkuPreviewPhotoPaths;

    /**
     * 主图文件路径
     */
    private String mainPhotoFilePath;

    /**
     * 商品主图
     */
    private String httpMainPhotoFilePath;


    /**
     * 商品介绍
     */
    private ShopProductDescriptionVO shopProductDescriptionVO;

    /**
     * SKU列表
     */
    private List<ProductSkuVO> productSkuVOList;

    /**
     * 运费模板ID
     */
    private Long freightTemplateId;

    private Short buckleInventoryMethod; //库存计数 1:买家拍下减库存 2:买家付款减库存


    /**
     * 介绍图路径
     */
    private String descriptionImgFilePath;

    /**
     * 商品购买状态
     */
    private List<ProductSkuBuyStatusVO> skuBuyStatusList;

    /**
     * 属性值状态
     */
    private AttributeValueStatusVO[][] attributeValueStatusVOS;

}
