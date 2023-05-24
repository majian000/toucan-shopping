package com.toucan.shopping.modules.product.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.toucan.shopping.modules.product.entity.ShopProduct;
import lombok.Data;

import java.util.List;

/**
 * 店铺维度的SKU (关联到平台维度的SPU)
 *
 * @author majian
 */
@Data
public class ShopProductVO extends ShopProduct {

    private String categoryName; //类别名称
    private String categoryPath; //类别路径

    private String shopCategoryName; //类别名称
    private String shopCategoryPath; //类别路径

    private String brandChineseName; //品牌中文名称
    private String brandEnglishName; //品牌英文名称
    private String brandLogo; //品牌logo
    private String brandHttpLogo; //品牌LOGO地址

    private String shopName; //店铺名称

    private String productSpuName; //关联平台商品名称

    private String productDescription; //商品介绍

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
     * SKU列表
     */
    private List<ProductSkuVO> productSkuVOList;

    /**
     * 验证码
     */
    private String vcode;

    /**
     * 审核人
     */
    private String createAdminId;


    /**
     * 商品介绍
     */
    private ShopProductDescriptionVO shopProductDescriptionVO;

    /**
     * 商品介绍JSON字符串
     */
    private String shopProductDescriptionJson="{}";

    /**
     * layui 表格中多选框默认选中
     */
    @JsonProperty("LAY_CHECKED")
    private boolean LAY_CHECKED = false;

    /**
     * ID列表
     */
    private List<Long> ids;

    /**
     * 指定属性路径查询
     */
    private String attrPath;


}
